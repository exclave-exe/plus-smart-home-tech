package ru.yandex.practicum.interaction.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.delivery.dto.AddressDto;

@AllArgsConstructor
@Setter
@Getter
public class CreateNewOrderRequest {

    @NotNull
    private String username;

    @NotNull
    @Valid
    private CartDto shoppingCart;

    @NotNull
    @Valid
    private AddressDto deliveryAddress;

}
