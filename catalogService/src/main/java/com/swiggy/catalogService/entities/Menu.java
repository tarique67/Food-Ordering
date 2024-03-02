package com.swiggy.catalogService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer menuId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private List<Item> items = new ArrayList<>();

    public void addItems(List<Item> items) {
        List<String> itemNames = items.stream().map(Item::getItemName).toList();

        this.items.removeIf(item -> itemNames.contains(item.getItemName()));

        this.items.addAll(items);
    }

    public boolean hasItem(Item item) {
        return items.contains(item);
    }
}
