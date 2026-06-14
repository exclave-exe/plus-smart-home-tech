package ru.yandex.practicum.interaction.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BookedProductsDto {

    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;

}
