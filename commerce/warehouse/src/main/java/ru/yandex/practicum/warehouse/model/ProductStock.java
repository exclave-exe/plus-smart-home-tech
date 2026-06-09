package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "product_stock")
@Setter
@Getter
public class ProductStock {

    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID productId;

    @Column(name = "width", nullable = false)
    private double width;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "depth", nullable = false)
    private double depth;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "fragile", nullable = false)
    private boolean fragile;

    @Column(name = "quantity", nullable = false)
    private long quantity;

}
