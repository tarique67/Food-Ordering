package com.swiggy.orderService.clients.fulfillmentServiceClients;

import com.swiggy.orderService.entities.Customer;
import com.swiggy.orderService.entities.Orders;
import com.swiggy.orderService.enums.OrderStatus;
import com.swiggy.orderService.exceptions.DeliveryExecutiveNotFoundException;
import com.swiggy.orderService.proto.AssignOrderRequest;
import com.swiggy.orderService.proto.AssignOrderResponse;
import com.swiggy.orderService.proto.FulfillmentServiceGrpc;
import com.swiggy.orderService.proto.Order;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;

public class FulfillmentServiceClient {

//        @Value("${converter.grpc.service.host}")
    private static final String fulfillmentServiceHost = "localhost";

    //    @Value("${converter.grpc.service.port}")
    private static final int port = 8083;

    public static AssignOrderResponse assignOrder(Orders order, String pickupLocation) throws DeliveryExecutiveNotFoundException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(fulfillmentServiceHost, port)
                .usePlaintext().build();

        FulfillmentServiceGrpc.FulfillmentServiceBlockingStub stub = FulfillmentServiceGrpc.newBlockingStub(channel);
        Order orderToAssign = Order.newBuilder().setOrderId(order.getOrderId()).setStatus(order.getStatus().toString()).
                setTotalPrice(order.getTotal_price().floatValue()).setPickUpLocation(pickupLocation).setDropLocation(order.getCustomer().getAddress())
                .build();
        AssignOrderRequest request = AssignOrderRequest.newBuilder().setOrder(orderToAssign).build();
        try{
            var response = stub.assignOrder(request);
            channel.shutdown();
            return response;
        }catch(Exception exception) {
            throw new DeliveryExecutiveNotFoundException("No delivery executive found at location " + pickupLocation);
        }
    }
}
