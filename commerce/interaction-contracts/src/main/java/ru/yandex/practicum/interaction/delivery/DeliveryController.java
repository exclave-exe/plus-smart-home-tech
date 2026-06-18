package ru.yandex.practicum.interaction.delivery;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryController {

    @PutMapping
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto delivery);

    @PostMapping("/successful")
    DeliveryDto successfulDelivery(@RequestBody UUID deliveryId);

    @PostMapping("/failed")
    DeliveryDto failedDelivery(@RequestBody UUID deliveryId);

    @PostMapping("/cost")
    BigDecimal costDelivery(@RequestBody @Valid OrderDto order);

    @PostMapping("/picked")
    DeliveryDto pickedDelivery(@RequestBody UUID deliveryId);

}
