package main

import (
    "context"
    // "fmt"
    // "net/http"
    // "net/http/httptest"
    "testing"

    pb "fulfillment_service/proto"
)

type MockDB struct{}

func (m *MockDB) CreateDeliveryExecutive(ctx context.Context, req *pb.CreateDeliveryExecutiveReuqest) (*pb.CreateDeliveryExecutiveResponse, error) {
    return &pb.CreateDeliveryExecutiveResponse{
        DeliveryExecutive: &pb.Deliver_Executive{
            DeliveryExecutiveId: 1,
            Name:                req.GetDeliveryExecutive().GetName(),
            Location:            req.GetDeliveryExecutive().GetLocation(),
            Token:               "mockToken",
        },
    }, nil
}

func (m *MockDB) AssignOrder(ctx context.Context, req *pb.AssignOrderRequest) (*pb.AssignOrderResponse, error) {
    order := req.GetOrder()
    order.Status = "ASSIGNED"
    order.DeliveryExecutiveId = 1
    return &pb.AssignOrderResponse{Order: order}, nil
}

func (m *MockDB) UpdateStatus(ctx context.Context, req *pb.UpdateStatusRequest) (*pb.UpdateStatusResponse, error) {
    orderID := req.GetOrderId()
    status := req.GetStatus()
    return &pb.UpdateStatusResponse{OrderId: orderID, Status: status}, nil
}

type MockServer struct {
    DB *MockDB
}

func (s *MockServer) CreateDeliveryExecutive(ctx context.Context, req *pb.CreateDeliveryExecutiveReuqest) (*pb.CreateDeliveryExecutiveResponse, error) {
    return s.DB.CreateDeliveryExecutive(ctx, req)
}

func (s *MockServer) AssignOrder(ctx context.Context, req *pb.AssignOrderRequest) (*pb.AssignOrderResponse, error) {
    return s.DB.AssignOrder(ctx, req)
}

func (s *MockServer) UpdateStatus(ctx context.Context, req *pb.UpdateStatusRequest) (*pb.UpdateStatusResponse, error) {
    return s.DB.UpdateStatus(ctx, req)
}

func TestCreateDeliveryExecutive(t *testing.T) {
    server := &MockServer{DB: &MockDB{}}

    req := &pb.CreateDeliveryExecutiveReuqest{
        DeliveryExecutive: &pb.Deliver_Executive{
            Name:     "John Doe",
            Location: "New York",
        },
    }

    resp, err := server.CreateDeliveryExecutive(context.Background(), req)
    if err != nil {
        t.Errorf("Unexpected error: %v", err)
    }

    if resp.DeliveryExecutive.Token != "mockToken" {
        t.Errorf("Token mismatch. Expected: %s, Got: %s", "mockToken", resp.DeliveryExecutive.Token)
    }
}

func TestAssignOrder(t *testing.T) {
    server := &MockServer{DB: &MockDB{}}

    req := &pb.AssignOrderRequest{
        Order: &pb.Order{
            OrderId:        1,
            PickUpLocation: "New York",
        },
    }

    resp, err := server.AssignOrder(context.Background(), req)
    if err != nil {
        t.Errorf("Unexpected error: %v", err)
    }

    if resp.Order.Status != "ASSIGNED" {
        t.Errorf("Expected status: %s, Got: %s", "ASSIGNED", resp.Order.Status)
    }
}

func TestUpdateStatus(t *testing.T) {
    server := &MockServer{DB: &MockDB{}}

    req := &pb.UpdateStatusRequest{
        OrderId: 1,
        Status:  "DELIVERED",
    }

    resp, err := server.UpdateStatus(context.Background(), req)
    if err != nil {
        t.Errorf("Unexpected error: %v", err)
    }

    if resp.Status != "DELIVERED" {
        t.Errorf("Expected status: %s, Got: %s", "DELIVERED", resp.Status)
    }
}

// func TestUpdateStatus_OrderServiceFailure(t *testing.T) {
//     server := &MockServer{DB: &MockDB{}}

//     req := &pb.UpdateStatusRequest{
//         OrderId: 1,
//         Status:  "DELIVERED",
//     }

//     // Mock HTTP server for updateOrderStatusInOrderService
//     ts := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
//         w.WriteHeader(http.StatusInternalServerError)
//     }))
//     defer ts.Close()

//     // Override the URL in the function with the mock server's URL
//     updateOrderStatusInOrderService = func(orderId int64, status string) error {
//         return fmt.Errorf("Order service failure")
//     }

//     _, err := server.UpdateStatus(context.Background(), req)
//     if err == nil {
//         t.Errorf("Expected an error but got nil")
//     }
// }
