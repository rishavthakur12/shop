package com.shop.order_service.service;


import com.shop.order_service.dto.InventoryResponseDto;
import com.shop.order_service.dto.OrderLineItemsDto;
import com.shop.order_service.dto.OrderRequestDto;
import com.shop.order_service.dto.OrderResponseDto;
import com.shop.order_service.event.OrderPlacedEvent;
import com.shop.order_service.model.Order;
import com.shop.order_service.model.OrderLineItems;
import com.shop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder; // <-- change here

    private final KafkaTemplate<String, OrderPlacedEvent>  kafkaTemplate;
    public void placeOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequestDto.getOrderLineItemsDto().stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes =  order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponseDto[] inventoryResponseArray = webClientBuilder.build().post()
                .uri("http://inventory-service/api/inventory")
                .bodyValue(orderRequestDto.getOrderLineItemsDto())   // send skuCode + quantity
                .retrieve()
                .bodyToMono(InventoryResponseDto[].class)
                .block();
        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponseDto::isInStock);
        if(allProductsInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product out of stock!");

        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getOrderId())
                        .orderNumber(order.getOrderNumber())
                        .orderLineItemsList(
                                order.getOrderLineItemsList().stream()
                                        .map(item -> new OrderLineItemsDto(
                                                item.getId(),
                                                item.getSkuCode(),
                                                item.getPrice(),
                                                item.getQuantity()
                                        ))
                                        .collect(Collectors.toList())
                        )
                        .build())
                .collect(Collectors.toList());
    }

}
