package ru.yandex.practicum.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.delivery.service.DeliveryService;
import ru.yandex.practicum.interaction.delivery.DeliveryController;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/delivery")
public class DeliveryControllerImpl implements DeliveryController {

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
