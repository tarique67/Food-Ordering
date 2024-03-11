package main

import (
	"context"
	"encoding/base64"
	"errors"
	"flag"
	"fmt"
	pb "fulfillment_service/proto"
	"log"
	"net"
	"net/http"
	"strings"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

func init() {
	DatabaseConnection()
}

type Status string

const (
	ACCEPTED  Status = "ACCEPTED"
	ASSIGNED  Status = "ASSIGNED"
	PICKED_UP Status = "PICKED_UP"
	DELIVERED Status = "DELIVERED"
)

type Order struct {
	OrderID             int64  `gorm:"primarykey"`
	DeliveryStatus      Status `gorm:"type:varchar(100)"`
	DeliveryExecutiveId int64
}

type Delivery_Executive struct {
	ID           int64 `gorm:"primarykey AUTO_INCREMENT"`
	Name         string
	Location     string
	OrderIds     []Order `gorm:"foreignkey:DeliveryExecutiveId"`
	Availability bool
	Password     string
}

func DatabaseConnection() *gorm.DB {
	host := "localhost"
	port := "5432"
	dbName := "fulfillment_service"
	dbUser := "postgres"
	password := "pass1234"
	dsn := fmt.Sprintf("host=%s port=%s user=%s dbname=%s password=%s sslmode=disable",
		host,
		port,
		dbUser,
		dbName,
		password,
	)
	DB, err := gorm.Open(postgres.Open(dsn), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info),
	})
	DB.Exec("DROP TABLE IF EXISTS orders;")
	DB.Exec("DROP TABLE IF EXISTS delivery_executives;")
	DB.AutoMigrate(Delivery_Executive{}, Order{})
	if err != nil {
		log.Fatal("Error connecting to the database...", err)
	}
	fmt.Println("Database connection successful...")
	return DB
}

var (
	port = flag.Int("port", 8083, "gRPC server port")
)

type server struct {
	DB *gorm.DB
	pb.UnimplementedFulfillmentServiceServer
}

func (s *server) CreateDeliveryExecutive(ctx context.Context, req *pb.CreateDeliveryExecutiveReuqest) (*pb.CreateDeliveryExecutiveResponse, error) {
	fmt.Println("Create Delivery Executive.")
	delivery_executive := req.GetDeliveryExecutive()

	data := Delivery_Executive{
		Name:         delivery_executive.GetName(),
		Location:     delivery_executive.GetLocation(),
		OrderIds:     []Order{},
		Availability: true,
		Password:     delivery_executive.GetPassword(),
	}

	res := s.DB.Create(&data)
	if res.Error != nil {
		errorString := fmt.Sprintf("Error storing the delivery: %v", res.Error)
		return nil, status.Errorf(codes.Unknown, errorString)
	}
	if res.RowsAffected == 0 {
		return nil, errors.New("user creation unsuccessful")
	}

	return &pb.CreateDeliveryExecutiveResponse{
		DeliveryExecutive: &pb.Deliver_Executive{
			DeliveryExecutiveId: data.ID,
			Name:                data.Name,
			Location:            data.Location,
			DeliveryOrders:      []*pb.Delivery_Order{},
			Password:            data.Password,
		},
	}, nil
}

func (s *server) AssignOrder(ctx context.Context, req *pb.AssignOrderRequest) (*pb.AssignOrderResponse, error) {
	fmt.Println("Assign Order to Delivery Executive.")
	order := req.GetOrder()

	var deliveryExecutive Delivery_Executive
	if err := s.DB.Where("location = ?  And availability = ?", order.PickUpLocation, true).First(&deliveryExecutive).Error; err != nil {
		return nil, status.Errorf(
			codes.NotFound,
			"could not find delivery executive with location %s: %v",
			order.PickUpLocation,
			err,
		)
	}

	assignedOrder := Order{
		OrderID:        order.GetOrderId(),
		DeliveryStatus: ASSIGNED,
	}

	deliveryExecutive.OrderIds = append(deliveryExecutive.OrderIds, assignedOrder)
	deliveryExecutive.Availability = false

	if err := s.DB.Save(&deliveryExecutive).Error; err != nil {
		return nil, status.Errorf(
			codes.Internal,
			"failed to update delivery executive: %v",
			err,
		)
	}

	order.Status = string(ASSIGNED)
	order.DeliveryExecutiveId = deliveryExecutive.ID

	return &pb.AssignOrderResponse{
		Order: order,
	}, nil
}

func (s *server) UpdateStatus(ctx context.Context, req *pb.UpdateStatusRequest) (*pb.UpdateStatusResponse, error) {
	fmt.Println("Update Status")

	delivery_executive, err := s.getCredentials(ctx)
	if err != nil {
		errorString := fmt.Sprintf("%v", err)
		return nil, status.Error(codes.Unauthenticated, errorString)
	}

	orderId := req.GetOrderId()
	status := req.GetStatus()

	var order Order
	if err := s.DB.Where("order_id = ?", orderId).First(&order).Error; err != nil {
		return nil, err
	}

	if order.DeliveryExecutiveId != delivery_executive.ID {
		return nil, fmt.Errorf("Order does not belong to this delivery executive.")
	}

	if err := s.DB.Model(&order).Update("delivery_status", status).Error; err != nil {
		return nil, err
	}

	if status == "DELIVERED" {
		var deliveryExecutive Delivery_Executive
		if err := s.DB.Where("id = ?", order.DeliveryExecutiveId).First(&deliveryExecutive).Error; err != nil {
			return nil, err
		}

		deliveryExecutive.Availability = true
		if err := s.DB.Save(&deliveryExecutive).Error; err != nil {
			return nil, err
		}
	}

	// Update the status in the order service
	if err := updateOrderStatusInOrderService(orderId, status); err != nil {
		return nil, err
	}

	return &pb.UpdateStatusResponse{
		OrderId: orderId,
		Status:  status,
	}, nil
}

func updateOrderStatusInOrderService(orderId int64, status string) error {
	url := fmt.Sprintf("http://localhost:8092/api/v1/orders/%d?status=%s", orderId, status)
	req, err := http.NewRequest(http.MethodPut, url, nil)
	if err != nil {
		return err
	}

	client := http.DefaultClient
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	return nil
}

func (s *server) getCredentials(ctx context.Context) (Delivery_Executive, error) {
	md, _ := metadata.FromIncomingContext(ctx)
	authHeader, ok := md["authorization"]

	if !ok || len(authHeader) == 0 {
		return Delivery_Executive{}, errors.New("authorization header not found")
	}

	authParts := strings.Fields(authHeader[0])
	if len(authParts) != 2 || authParts[0] != "Basic" {
		return Delivery_Executive{}, errors.New("invalid Authorization header format")
	}

	decodedCredentials, err := base64.StdEncoding.DecodeString(authParts[1])
	if err != nil {
		return Delivery_Executive{}, errors.New("error decoding base64 credentials")
	}

	credentials := strings.SplitN(string(decodedCredentials), ":", 2)
	if len(credentials) != 2 {
		return Delivery_Executive{}, errors.New("invalid credentials format")
	}

	username := credentials[0]
	password := credentials[1]

	var user Delivery_Executive
	err = s.DB.Where("name = ? AND password = ?", username, password).First(&user).Error
	if err != nil {
		return Delivery_Executive{}, errors.New("invalid credentials")
	}

	return user, nil
}

func main() {
	fmt.Println("gRPC server running ...")

	lis, err := net.Listen("tcp", fmt.Sprintf(":%d", *port))

	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}

	s := grpc.NewServer()
	db := DatabaseConnection()
	pb.RegisterFulfillmentServiceServer(s, &server{DB: db})

	log.Printf("Server listening at %v", lis.Addr())

	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve : %v", err)
	}
}
