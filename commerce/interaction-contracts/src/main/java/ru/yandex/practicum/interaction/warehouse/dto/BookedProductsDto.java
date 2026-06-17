package ru.yandex.practicum.interaction.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookedProductsDto {

    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;

}
