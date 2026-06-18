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

    private UUID paymentId;
    private UUID orderId;
    private Double totalPayment;
    private Double deliveryTotal;
    private Double feeTotal;
    private PaymentState paymentState;

}
