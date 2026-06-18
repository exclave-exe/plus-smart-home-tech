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

public interface OrderController {

    @GetMapping
    List<OrderDto> getOrders(@RequestParam @NotBlank String username);

    @PutMapping
    OrderDto createOrder(@RequestBody @NotNull @Valid CreateNewOrderRequest createOrderRequest);

    @PostMapping("return")
    OrderDto returnOrder(@RequestBody @NotNull @Valid ProductReturnRequest returnOrderRequest);

    @PostMapping("payment")
    OrderDto paymentOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("payment/failed")
    OrderDto paymentFailedOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("delivery")
    OrderDto deliveryOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("delivery/failed")
    OrderDto deliveryFailedOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("completed")
    OrderDto completedOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("calculated/total")
    OrderDto calculatedTotalOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("calculated/delivery")
    OrderDto calculatedDeliveryOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("assembly")
    OrderDto assemblyOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("assembly/failed")
    OrderDto assemblyFailedOrder(@RequestBody @NotNull UUID orderId);

}
