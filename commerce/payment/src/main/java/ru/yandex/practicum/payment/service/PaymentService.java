package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto order);

    BigDecimal totalCostPayment(OrderDto order);

    void refundPayment(UUID paymentId);

    BigDecimal productCostPayment(OrderDto order);

    void failedPayment(UUID paymentId);

}
