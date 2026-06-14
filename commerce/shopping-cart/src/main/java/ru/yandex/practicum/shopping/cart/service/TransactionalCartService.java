package ru.yandex.practicum.shopping.cart.service;

import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.cart.dto.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TransactionalCartService {

    CartDto getCart(String username);

    CartDto addProductsToCart(String username, Map<UUID, Long> products);

    CartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);

    CartDto removeProductsFromCart(String username, List<UUID> productIds);

    void deactivateCart(String username);

}
