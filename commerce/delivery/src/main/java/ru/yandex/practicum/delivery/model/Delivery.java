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
    UUID deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address_id", referencedColumnName = "address_id")
    Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id", referencedColumnName = "address_id")
    Address toAddress;

    UUID orderId;

    @Enumerated(EnumType.STRING)
    DeliveryState deliveryState;
}
