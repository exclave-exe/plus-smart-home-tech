package ru.yandex.practicum.interaction.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class AssemblyProductsForOrderRequest {

    @NotNull
    UUID orderId;

    @NotNull
    Map<UUID, Long> products;

}
