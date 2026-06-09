package ru.yandex.practicum.interaction.warehouse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class NewProductInWarehouseRequest {

    @NotNull
    private UUID productId;

    @NotNull
    @Valid
    private DimensionDto dimension;

    private Boolean fragile;

    @NotNull
    @DecimalMin(value = "1.0", inclusive = true)
    private Double weight;

}
