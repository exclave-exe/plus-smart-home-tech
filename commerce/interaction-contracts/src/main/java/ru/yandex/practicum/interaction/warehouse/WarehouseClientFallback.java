package ru.yandex.practicum.interaction.warehouse;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.warehouse.dto.NewProductInWarehouseRequest;

@Component
public class WarehouseClientFallback implements WarehouseClient {
    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        throw new RuntimeException("Сервис warehouse не доступен");
    }

    @Override
    public void addQuantity(AddProductToWarehouseRequest request) {
        throw new RuntimeException("Сервис warehouse не доступен");
    }

    @Override
    public BookedProductsDto checkQuantity(CartDto cartDto) {
        throw new RuntimeException("Сервис warehouse не доступен");
    }

    @Override
    public AddressDto getWarehouseAddress() {
        throw new RuntimeException("Сервис warehouse не доступен");
    }
}
