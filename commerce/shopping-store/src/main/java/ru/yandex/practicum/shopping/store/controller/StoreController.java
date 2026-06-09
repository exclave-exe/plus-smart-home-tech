package ru.yandex.practicum.shopping.store.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.enums.ProductCategory;
import ru.yandex.practicum.interaction.enums.QuantityState;
import ru.yandex.practicum.shopping.store.dto.ProductDto;
import ru.yandex.practicum.shopping.store.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Validated
public class ShoppingStoreController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getProductsByCategory(
            @RequestParam
            ProductCategory category,

            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Номер страницы не может быть отрицательным")
            int page,

            @RequestParam(defaultValue = "20")
            @Positive(message = "Размер страницы должен быть положительным")
            int size,

            @RequestParam(required = false)
            String[] sort
    ) {
        return productService.getProductsByCategory(category, page, size, sort);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto createProduct(@RequestBody @NotNull @Valid ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@RequestBody @NotNull @Valid ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public Boolean setQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
        return productService.setQuantityState(productId, quantityState);
    }

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public Boolean removeProductFromStore(@RequestBody @NotNull UUID productId) {
        return productService.removeProductFromStore(productId);
    }

}