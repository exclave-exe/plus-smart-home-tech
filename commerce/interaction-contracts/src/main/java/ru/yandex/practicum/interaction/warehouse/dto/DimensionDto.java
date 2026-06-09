package ru.yandex.practicum.interaction.warehouse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class DimensionDto {

    @NotNull
    @DecimalMin(value = "1.0", inclusive = true)
    private double width;

    @NotNull
    @DecimalMin(value = "1.0", inclusive = true)
    private double height;

    @NotNull
    @DecimalMin(value = "1.0", inclusive = true)
    private double depth;

}
