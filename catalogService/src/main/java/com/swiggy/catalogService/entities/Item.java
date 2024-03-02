package com.swiggy.catalogService.entities;

import com.swiggy.catalogService.enums.ItemStatus;
import com.swiggy.catalogService.exceptions.InvalidPriceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.swiggy.catalogService.constants.ExceptionMessages.NEGATIVE_PRICE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer itemId;
    private String itemName;
    private Double price;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    public Item(String itemName, Double price, ItemStatus status) {
        this.itemName = itemName;
        this.price = price;
        this.status = status;
    }

    public void updatePrice(double price) throws InvalidPriceException {
        if(price<=0)
            throw new InvalidPriceException(NEGATIVE_PRICE);
        this.price = price;
    }

    public void updateStatus(ItemStatus status){
        this.status = status;
    }
}
