package ru.yandex.practicum.interaction.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class ProductReturnRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private Map<UUID, Long> products;

}