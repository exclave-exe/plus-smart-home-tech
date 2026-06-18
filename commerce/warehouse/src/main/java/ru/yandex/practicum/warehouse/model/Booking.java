package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Entity
@Data
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(name = "fragile")
    private boolean fragile;

    @Column(name = "delivery_volume")
    private double deliveryVolume;

    @Column(name = "delivery_weight")
    private double deliveryWeight;

    @ElementCollection
    @CollectionTable(name = "booking_products", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "order_id")
    private UUID orderId;
}
