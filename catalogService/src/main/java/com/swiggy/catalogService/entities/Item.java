package com.swiggy.catalogService.entities;

import com.swiggy.catalogService.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
