package ru.yandex.practicum.interaction.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class CartDto {

    @NotNull
    private UUID shoppingCartId;

    @NotNull
    private Map<UUID, Long> products;

}
