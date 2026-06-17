package ru.yandex.practicum.interaction.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentOperations {

    @PostMapping
    PaymentDto createPayment(@RequestBody @NotNull @Valid OrderDto order);

    @PostMapping("/totalCost")
    BigDecimal totalCostPayment(@RequestBody @NotNull @Valid OrderDto order);

    @PostMapping("/refund")
    void refundPayment(@RequestBody @NotNull UUID paymentId);

    @PostMapping("/productCost")
    BigDecimal productCostPayment(@RequestBody @NotNull @Valid OrderDto order);

    @PostMapping("/failed")
    void failedPayment(@RequestBody @NotNull UUID paymentId);

}
