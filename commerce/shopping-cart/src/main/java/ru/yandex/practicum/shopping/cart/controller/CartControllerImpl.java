package ru.yandex.practicum.shopping.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.cart.ShoppingCartController;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.shopping.cart.service.NonTransactionalCartService;
import ru.yandex.practicum.shopping.cart.service.TransactionalCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class CartControllerImpl implements ShoppingCartController {

    private final TransactionalCartService transactionalCartService;
    private final NonTransactionalCartService nonTransactionalCartService;

    @Override
    public CartDto getCart(String username) {
        return transactionalCartService.getCart(username);
    }

    @Override
    public CartDto addProductsToCart(String username, Map<UUID, Long> products) {
        System.out.println("addProductsToCart called, username=" + username + ", products=" + products);
        return nonTransactionalCartService.addProductsToCart(username, products);
    }

    @Override
    public CartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        return nonTransactionalCartService.changeProductQuantity(username, request);
    }

    @Override
    public CartDto removeProductsFromCart(String username, List<UUID> productIds) {
        return transactionalCartService.removeProductsFromCart(username, productIds);
    }

    @Override
    public void deactivateCart(String username) {
        transactionalCartService.deactivateCart(username);
    }

}