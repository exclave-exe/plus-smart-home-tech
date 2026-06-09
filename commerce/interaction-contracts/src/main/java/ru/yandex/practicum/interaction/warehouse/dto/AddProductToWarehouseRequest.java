package ru.yandex.practicum.warehouse.dto;

import lombok.Data;

public class AddProductToWarehouseRequest {
    private String productId;
    private Long quantity;
}
