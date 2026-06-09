package ru.yandex.practicum.shopping.cart.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.shopping.cart.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);

}
