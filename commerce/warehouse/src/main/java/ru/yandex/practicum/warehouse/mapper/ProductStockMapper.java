package ru.yandex.practicum.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.yandex.practicum.interaction.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.model.ProductStock;

@Mapper(componentModel = "spring")
public interface ProductStockMapper {

    @Mapping(target = "width", source = "dimension.width")
    @Mapping(target = "height", source = "dimension.height")
    @Mapping(target = "depth", source = "dimension.depth")
    @Mapping(target = "quantity", constant = "0L")
    @Mapping(target = "fragile", source = "fragile", qualifiedByName = "ifNullThenFalse")
    ProductStock toEntity(NewProductInWarehouseRequest request);

    @Named("ifNullThenFalse")
    default boolean ifNullThenFalse(Boolean fragile) {
        return fragile != null && fragile;
    }

}
