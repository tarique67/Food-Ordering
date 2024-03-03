package com.swiggy.orderService.clients.catalogServiceClients.responseModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swiggy.orderService.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {

    private Integer menuId;
    private List<Item> items;
}
