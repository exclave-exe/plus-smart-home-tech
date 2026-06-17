package ru.yandex.practicum.order.service;

import ru.yandex.practicum.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.order.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getOrders(String username);

    OrderDto createOrder(CreateNewOrderRequest createOrderRequest);

    OrderDto returnOrder(ProductReturnRequest returnOrderRequest);

    OrderDto paymentOrder(UUID orderId);

    OrderDto paymentFailedOrder(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto deliveryFailedOrder(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculatedTotalOrder(UUID orderId);

    OrderDto calculatedDeliveryOrder(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto assemblyFailedOrder(UUID orderId);

}