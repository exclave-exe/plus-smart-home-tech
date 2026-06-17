package ru.yandex.practicum.interaction.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.interaction.delivery.enums.DeliveryState;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class DeliveryDto {

    UUID deliveryId;

    @NotNull
    AddressDto fromAddress;

    @NotNull
    AddressDto toAddress;

    @NotNull
    UUID orderId;

    DeliveryState deliveryState;
}