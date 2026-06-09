package ru.yandex.practicum.interaction.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class AddProductToWarehouseRequest {

    @NotNull
    private UUID productId;

    @NotNull
    @Positive
    private Long quantity;

}
