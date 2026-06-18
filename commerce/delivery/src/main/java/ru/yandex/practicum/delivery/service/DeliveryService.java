package ru.yandex.practicum.delivery.service;

import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto delivery);

    DeliveryDto successfulDelivery(UUID deliveryId);

    DeliveryDto failedDelivery(UUID deliveryId);

    BigDecimal costDelivery(OrderDto order);

    DeliveryDto pickedDelivery(UUID deliveryId);
}
