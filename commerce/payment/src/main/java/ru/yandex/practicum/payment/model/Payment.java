package ru.yandex.practicum.payment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.interaction.payment.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Setter
@Getter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @Column(name = "delivery_total")
    private BigDecimal deliveryTotal;

    @Column(name = "fee_total")
    private BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState paymentState;


}
