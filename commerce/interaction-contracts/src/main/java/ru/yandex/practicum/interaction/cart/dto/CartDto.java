package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Setter @Getter
public class CartDto {

    @NotNull
    private UUID ShoppingCartId;

    @NotNull
    private Map<UUID, Integer> products = new HashMap<>();
}
