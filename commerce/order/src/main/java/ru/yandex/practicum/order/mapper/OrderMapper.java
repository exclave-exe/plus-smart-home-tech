package ru.yandex.practicum.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.order.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order product);

    Order toEntity(OrderDto dto);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "shoppingCartId", expression = "java(request.getShoppingCart().getShoppingCartId())")
    @Mapping(target = "products", expression = "java(request.getShoppingCart().getProducts())")
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "deliveryId", ignore = true)
    @Mapping(target = "state", expression = "java(ru.yandex.practicum.interaction.order.enums.OrderState.NEW)")
    @Mapping(target = "deliveryWeight", source = "bookedProducts.deliveryWeight")
    @Mapping(target = "deliveryVolume", source = "bookedProducts.deliveryVolume")
    @Mapping(target = "fragile", source = "bookedProducts.fragile")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "deliveryPrice", ignore = true)
    @Mapping(target = "productPrice", ignore = true)
    Order toOrderFromAssembly(CreateNewOrderRequest request, BookedProductsDto bookedProducts);

}
