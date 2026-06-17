package ru.yandex.practicum.interaction.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.order.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderOperations {

    // Получить заказы пользователя
    @GetMapping
    List<OrderDto> getOrders(@RequestParam @NotBlank String username);

    // Создать новый заказ в системе
    @PutMapping
    OrderDto createOrder(@RequestBody @NotNull @Valid CreateNewOrderRequest createOrderRequest);

    // Возврат заказа
    @PostMapping("return")
    OrderDto returnOrder(@RequestBody @NotNull @Valid ProductReturnRequest returnOrderRequest);

    // Оплата заказа
    @PostMapping("payment")
    OrderDto paymentOrder(@RequestBody @NotNull UUID orderId);

    // Оплата заказа произошла с ошибкой
    @PostMapping("payment/failed")
    OrderDto paymentFailedOrder(@RequestBody @NotNull UUID orderId);

    // Доставка заказа
    @PostMapping("delivery")
    OrderDto deliveryOrder(@RequestBody @NotNull UUID orderId);

    // Доставка заказа произошла с ошибкой
    @PostMapping("delivery/failed")
    OrderDto deliveryFailedOrder(@RequestBody @NotNull UUID orderId);

    // Завершение заказа.
    @PostMapping("completed")
    OrderDto completedOrder(@RequestBody @NotNull UUID orderId);

    // Расчёт стоимости заказа
    @PostMapping("calculated/total")
    OrderDto calculatedTotalOrder(@RequestBody @NotNull UUID orderId);

    // Расчёт стоимости доставки заказа
    @PostMapping("calculated/delivery")
    OrderDto calculatedDeliveryOrder(@RequestBody @NotNull UUID orderId);

    // Сборка заказа
    @PostMapping("assembly")
    OrderDto assemblyOrder(@RequestBody @NotNull UUID orderId);

    // Сборка заказа произошла с ошибкой
    @PostMapping("assembly/failed")
    OrderDto assemblyFailedOrder(@RequestBody @NotNull UUID orderId);

}
