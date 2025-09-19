package com.shop.inventory_service.service;


import com.shop.inventory_service.dto.InventoryRequestDto;
import com.shop.inventory_service.dto.InventoryResponseDto;
import com.shop.inventory_service.model.Inventory;
import com.shop.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponseDto> isInStock(List<InventoryRequestDto> requestList) {
        List<String> skuCodes = requestList.stream()
                .map(InventoryRequestDto::getSkuCode)
                .toList();

        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        return requestList.stream()
                .map(request -> {
                    return inventories.stream()
                            .filter(i -> i.getSkuCode().equals(request.getSkuCode()))
                            .findFirst()
                            .map(inv -> InventoryResponseDto.builder()
                                    .skuCode(request.getSkuCode())
                                    .isInStock(inv.getQuantity() >= request.getQuantity())
                                    .build())
                            .orElse(InventoryResponseDto.builder()
                                    .skuCode(request.getSkuCode())
                                    .isInStock(false) // not found → treat as out of stock
                                    .build());
                })
                .toList();

    }

}
