package ru.yandex.practicum.shopping.store.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.interaction.store.dto.ProductDto;
import ru.yandex.practicum.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.interaction.store.enums.QuantityState;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDto getProduct(UUID ProductId);

    List<ProductDto> getProductsByIds(List<UUID> ids);

    Page<ProductDto> getProductsByCategory(ProductCategory category, int page, int size, String[] sort);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean setQuantityState(UUID ProductId, QuantityState quantityState);

    Boolean removeProductFromStore(UUID productId);

}
