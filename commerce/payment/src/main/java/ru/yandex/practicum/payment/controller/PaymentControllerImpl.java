package ru.yandex.practicum.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.payment.PaymentController;
import ru.yandex.practicum.interaction.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class PaymentControllerImpl implements PaymentController {

    private final PaymentService service;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        return service.createPayment(order);
    }

    @Override
    public BigDecimal totalCostPayment(OrderDto order) {
        return service.totalCostPayment(order);
    }

    @Override
    public BigDecimal productCostPayment(OrderDto order) {
        return service.productCostPayment(order);
    }

    @Override
    public void refundPayment(UUID paymentId) {
        service.refundPayment(paymentId);
    }

    @Override
    public void failedPayment(UUID paymentId) {
        service.failedPayment(paymentId);
    }
}
