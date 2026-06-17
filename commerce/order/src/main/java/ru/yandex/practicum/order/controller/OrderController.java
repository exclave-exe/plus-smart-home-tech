package ru.yandex.practicum.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.order.OrderOperations;
import ru.yandex.practicum.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.order.dto.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderOperations {

    private final OrderService service;

    @Override
    public List<OrderDto> getOrders(String username) {
        return service.getOrders(username);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest createOrderRequest) {
        return service.createOrder(createOrderRequest);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest returnOrderRequest) {
        return service.returnOrder(returnOrderRequest);
    }

    @Override
    public OrderDto paymentOrder(UUID orderId) {
        return service.paymentOrder(orderId);
    }

    @Override
    public OrderDto paymentFailedOrder(UUID orderId) {
        return service.paymentFailedOrder(orderId);
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        return service.deliveryOrder(orderId);
    }

    @Override
    public OrderDto deliveryFailedOrder(UUID orderId) {
        return service.deliveryFailedOrder(orderId);
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {
        return service.completedOrder(orderId);
    }

    @Override
    public OrderDto calculatedTotalOrder(UUID orderId) {
        return service.calculatedTotalOrder(orderId);
    }

    @Override
    public OrderDto calculatedDeliveryOrder(UUID orderId) {
        return service.calculatedDeliveryOrder(orderId);
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        return service.assemblyOrder(orderId);
    }

    @Override
    public OrderDto assemblyFailedOrder(UUID orderId) {
        return service.assemblyFailedOrder(orderId);
    }

}
