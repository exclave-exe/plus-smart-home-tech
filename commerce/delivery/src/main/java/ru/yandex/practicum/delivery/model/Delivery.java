package ru.yandex.practicum.delivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.interaction.delivery.enums.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Setter
@Getter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address_id", referencedColumnName = "address_id")
    private Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id", referencedColumnName = "address_id")
    private Address toAddress;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;
}
