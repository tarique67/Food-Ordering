package main

import (
	"context"
	"errors"
	"flag"
	"fmt"
	pb "fulfillment_service/proto"
	"log"
	"net"

	"github.com/google/uuid"
	"google.golang.org/grpc"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func init() {
	DatabaseConnection()
}

type Status string

const (
	ACCEPTED Status = "ACCEPTED"
	ASSIGNED Status = "ASSIGNED"
	PICKED_UP Status = "PICKED_UP"
	DELIVERED Status = "DELIVERED"
)

type Order struct{
	gorm.Model
	OrderID int64 `gorm:"primarykey"`
	DeliveryStatus Status 
}

type Delivery_Executive struct {
	gorm.Model
	ID        string `gorm:"primarykey"`
	Name      string
	Location  string
	OrderIds  []Order `gorm:"foreignkey:OrderID"`
	Token     string
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
	DB, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
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
	delivery_executive.DeliveryExecutiveId = uuid.New().String()
	delivery_executive.Token = uuid.New().String()

	data := Delivery_Executive{
		ID:    delivery_executive.GetDeliveryExecutiveId(),
		Name:  delivery_executive.GetName(),
		Location: delivery_executive.GetLocation(),
		OrderIds: []Order{},
		Token: delivery_executive.GetToken(),
	}

	res := s.DB.Create(&data)
	if res.RowsAffected == 0 {
		return nil, errors.New("user creation unsuccessful")
	}

	return &pb.CreateDeliveryExecutiveResponse{
		DeliveryExecutive: &pb.Deliver_Executive{
			DeliveryExecutiveId:    data.ID,
			Name:  data.Name,
			Location: data.Location,
			DeliveryOrders: []*pb.Delivery_Order{},
			Token: data.Token,
		},
	}, nil
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