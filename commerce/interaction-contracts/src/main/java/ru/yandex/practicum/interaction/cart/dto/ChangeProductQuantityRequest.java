package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter @Getter
public class ChangeProductQuantityRequest {

    @NotNull(message = "Идентификатор товара не может быть пустым")
    private UUID productId;

    @PositiveOrZero(message = "Количество должно быть положительным или равно 0")
    private Integer newQuantity;
}