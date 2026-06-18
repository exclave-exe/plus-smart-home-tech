package ru.yandex.practicum.interaction.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.interaction.order.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
public class OrderDto {

    @NotNull
    private UUID orderId;
    private UUID shoppingCartId;

    @NotNull
    private Map<UUID, Long> products;

    private UUID paymentId;
    private UUID deliveryId;

    private OrderState state;

    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;

    private BigDecimal totalPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal productPrice;

}
