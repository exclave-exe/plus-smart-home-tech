package ru.yandex.practicum.shopping.store.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.interaction.store.dto.ProductDto;
import ru.yandex.practicum.shopping.store.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto dto);

}
