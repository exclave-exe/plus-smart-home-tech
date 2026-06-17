package ru.yandex.practicum.interaction.delivery;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryOperations {

    // Создать новую доставку в БД
    @PutMapping
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto delivery);

    // Эмуляция успешной доставки товара
    @PostMapping("/successful")
    DeliveryDto successfulDelivery(@RequestBody UUID deliveryId);

    // Эмуляция неудачного вручения товара
    @PostMapping("/failed")
    DeliveryDto failedDelivery(@RequestBody UUID deliveryId);

    // Расчёт полной стоимости доставки заказа
    @PostMapping("/cost")
    BigDecimal costDelivery(@RequestBody @Valid OrderDto order);

    // Эмуляция получения товара в доставку
    @PostMapping("/picked")
    DeliveryDto pickedDelivery(@RequestBody UUID deliveryId);

}
