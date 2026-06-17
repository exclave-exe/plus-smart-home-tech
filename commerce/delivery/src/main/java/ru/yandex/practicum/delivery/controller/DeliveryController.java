package ru.yandex.practicum.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.delivery.service.DeliveryService;
import ru.yandex.practicum.interaction.delivery.DeliveryOperations;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DeliveryController implements DeliveryOperations {

    private final DeliveryService service;

    @Override
    public DeliveryDto createDelivery(DeliveryDto delivery) {
        return service.createDelivery(delivery);
    }

    @Override
    public DeliveryDto successfulDelivery(UUID deliveryId) {
        return service.successfulDelivery(deliveryId);
    }

    @Override
    public DeliveryDto failedDelivery(UUID deliveryId) {
        return service.failedDelivery(deliveryId);
    }

    @Override
    public BigDecimal costDelivery(OrderDto order) {
        return service.costDelivery(order);
    }

    @Override
    public DeliveryDto pickedDelivery(UUID deliveryId) {
        return service.pickedDelivery(deliveryId);
    }

}
