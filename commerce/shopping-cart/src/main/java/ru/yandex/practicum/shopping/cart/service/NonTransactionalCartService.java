package ru.yandex.practicum.shopping.cart.service;

import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.cart.dto.ChangeProductQuantityRequest;

import java.util.Map;
import java.util.UUID;

public interface NonTransactionalCartService {

    CartDto addProductsToCart(String username, Map<UUID, Long> products);

    CartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);

}
