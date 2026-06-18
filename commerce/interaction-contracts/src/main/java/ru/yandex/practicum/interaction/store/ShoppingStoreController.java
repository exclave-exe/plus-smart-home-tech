package ru.yandex.practicum.interaction.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.store.dto.ProductDto;
import ru.yandex.practicum.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.interaction.store.enums.QuantityState;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreController {

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @PostMapping("/products/batch")
    List<ProductDto> getProductsByIds(@RequestBody List<UUID> ids);

    @GetMapping
    Page<ProductDto> getProductsByCategory(
            @RequestParam ProductCategory category,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "20") @Positive int size,
            @RequestParam(required = false) String[] sort
    );

    @PutMapping
    ProductDto createProduct(@RequestBody @NotNull @Valid ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody @NotNull @Valid ProductDto productDto);

    @PostMapping("/quantityState")
    Boolean setQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);

    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody @NotNull UUID productId);

}