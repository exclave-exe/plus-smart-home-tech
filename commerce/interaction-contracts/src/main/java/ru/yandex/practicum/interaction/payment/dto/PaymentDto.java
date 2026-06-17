package ru.yandex.practicum.interaction.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.interaction.payment.enums.PaymentState;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class PaymentDto {

    UUID paymentId;
    UUID orderId;
    Double totalPayment;
    Double deliveryTotal;
    Double feeTotal;
    PaymentState paymentState;

}
