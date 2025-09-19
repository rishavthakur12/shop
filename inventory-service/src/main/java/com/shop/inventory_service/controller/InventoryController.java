package com.shop.inventory_service.controller;


import com.shop.inventory_service.dto.InventoryRequestDto;
import com.shop.inventory_service.dto.InventoryResponseDto;
import com.shop.inventory_service.model.Inventory;
import com.shop.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> isInStock(@RequestBody List<InventoryRequestDto> orderLineItems) {
        return inventoryService.isInStock(orderLineItems);
    }
}
