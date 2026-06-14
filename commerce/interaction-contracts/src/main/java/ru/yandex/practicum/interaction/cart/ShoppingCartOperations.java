package ru.yandex.practicum.interaction.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.cart.dto.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartOperations {

    @GetMapping
    CartDto getCart(@RequestParam @NotBlank String username);

    @PutMapping
    CartDto addProductsToCart(
            @RequestParam @NotBlank String username,
            @RequestBody @NotNull Map<UUID, Long> products
    );

    @PostMapping("/change-quantity")
    CartDto changeProductQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody @NotNull @Valid ChangeProductQuantityRequest request
    );

    @PostMapping("/remove")
    CartDto removeProductsFromCart(
            @RequestParam @NotBlank String username,
            @RequestBody @NotNull List<UUID> productIds
    );

    @DeleteMapping
    void deactivateCart(@RequestParam @NotBlank String username);

}