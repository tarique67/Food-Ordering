package com.swiggy.orderService.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiggy.orderService.clients.catalogServiceClients.CatalogServiceClient;
import com.swiggy.orderService.clients.catalogServiceClients.responseModels.Restaurant;
import com.swiggy.orderService.clients.fulfillmentServiceClients.FulfillmentServiceClient;
import com.swiggy.orderService.enums.OrderStatus;
import com.swiggy.orderService.exceptions.DeliveryExecutiveNotFoundException;
import com.swiggy.orderService.exceptions.ItemNotInRestaurantException;
import com.swiggy.orderService.proto.AssignOrderResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.swiggy.orderService.constants.ExceptionMessages.ITEM_NOT_IN_RESTAURANT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;

    private Integer restaurantId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double total_price;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    private Integer deliveryExecutiveId;

    public void create(int restaurantId, List<String> items, Customer customer) throws JsonProcessingException, ItemNotInRestaurantException {
        Restaurant restaurant = new CatalogServiceClient().fetchRestaurantFromCatalogService(restaurantId);
        List<String> restaurantItems = restaurant.getMenu().getItems().stream().map((Item::getItemName)).toList();
        List<Item> itemsToOrder = new ArrayList<>();
        for(String item : items){
            if(!restaurantItems.contains(item))
                throw new ItemNotInRestaurantException(ITEM_NOT_IN_RESTAURANT + item);
            itemsToOrder.add(restaurant.getMenu().getItems().get(restaurantItems.indexOf(item)));
        }
        double total_price = itemsToOrder.stream().mapToDouble(Item::getPrice).sum();
        this.restaurantId = restaurantId;
        this.items = itemsToOrder;
        this.total_price = total_price;
        this.status = OrderStatus.ACCEPTED;
        this.customer = customer;
    }

    public void assignDeliveryExecutive(int restaurantId) throws JsonProcessingException, DeliveryExecutiveNotFoundException {
        Restaurant restaurant = new CatalogServiceClient().fetchRestaurantFromCatalogService(restaurantId);
        AssignOrderResponse response = FulfillmentServiceClient.assignOrder(this, restaurant.getAddress());
        this.deliveryExecutiveId = (int)response.getOrder().getDeliveryExecutiveId();
        this.status = OrderStatus.valueOf(response.getOrder().getStatus());
    }
}
