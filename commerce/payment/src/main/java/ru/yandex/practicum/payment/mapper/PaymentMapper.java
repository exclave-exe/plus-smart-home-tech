package ru.yandex.practicum.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.model.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "totalPayment", expression = "java(order.getTotalPrice())")
    @Mapping(target = "deliveryTotal", expression = "java(order.getDeliveryPrice())")
    @Mapping(target = "feeTotal", expression = "java(order.getProductPrice())")
    @Mapping(target = "paymentState", expression = "java(PaymentState.PENDING)")
    Payment toPayment(OrderDto order);

    PaymentDto toPaymentDto(Payment payment);

}