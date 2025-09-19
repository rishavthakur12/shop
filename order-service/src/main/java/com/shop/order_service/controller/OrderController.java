package com.shop.order_service.controller;


import com.shop.order_service.dto.OrderRequestDto;
import com.shop.order_service.dto.OrderResponseDto;
import com.shop.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
//    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
//    @TimeLimiter(name = "inventory")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            orderService.placeOrder(orderRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order Placed successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("getOrders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    public ResponseEntity<String> fallbackMethod(OrderRequestDto orderRequestDto, Throwable e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Oops! Something went wrong, we'll be back soon!");
    }
}
