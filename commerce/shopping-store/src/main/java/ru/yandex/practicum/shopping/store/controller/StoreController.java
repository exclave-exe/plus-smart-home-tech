package ru.yandex.practicum.shopping.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.store.ShoppingStoreOperations;
import ru.yandex.practicum.interaction.store.dto.ProductDto;
import ru.yandex.practicum.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.interaction.store.enums.QuantityState;
import ru.yandex.practicum.shopping.store.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class StoreController implements ShoppingStoreOperations {

    private final ProductService productService;

    @Override
    public ProductDto getProduct(UUID productId) {
        return productService.getProduct(productId);
    }

    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, int page, int size, String[] sort) {
        return productService.getProductsByCategory(category, page, size, sort);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @Override
    public Boolean setQuantityState(UUID productId, QuantityState quantityState) {
        return productService.setQuantityState(productId, quantityState);
    }

    @Override
    public Boolean removeProductFromStore(UUID productId) {
        return productService.removeProductFromStore(productId);
    }

}