package ru.yandex.practicum.interaction.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class ChangeProductQuantityRequest {

    @NotNull(message = "Идентификатор товара не может быть пустым")
    private UUID productId;

    @PositiveOrZero(message = "Количество должно быть положительным или равно 0")
    private Long newQuantity;

}