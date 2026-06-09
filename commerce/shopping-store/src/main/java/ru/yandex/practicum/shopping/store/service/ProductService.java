package ru.yandex.practicum.shopping.store.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.interaction.store.dto.ProductDto;
import ru.yandex.practicum.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.interaction.store.enums.QuantityState;

import java.util.UUID;

public interface ProductService {

    public ProductDto getProduct(UUID ProductId);

    public Page<ProductDto> getProductsByCategory(ProductCategory category, int page, int size, String[] sort);

    public ProductDto createProduct(ProductDto productDto);

    public ProductDto updateProduct(ProductDto productDto);

    public Boolean setQuantityState(UUID ProductId, QuantityState quantityState);

    public Boolean removeProductFromStore(UUID productId);

}
