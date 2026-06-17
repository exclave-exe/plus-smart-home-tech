package ru.yandex.practicum.delivery.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto delivery);

    DeliveryDto successfulDelivery(@RequestBody UUID deliveryId);

    DeliveryDto failedDelivery(@RequestBody UUID deliveryId);

    BigDecimal costDelivery(@RequestBody @Valid OrderDto order);

    DeliveryDto pickedDelivery(@RequestBody UUID deliveryId);
}
