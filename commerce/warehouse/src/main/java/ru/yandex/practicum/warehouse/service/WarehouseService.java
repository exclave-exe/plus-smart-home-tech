package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.warehouse.dto.NewProductInWarehouseRequest;

public interface WarehouseService {

    public void addNewProduct(NewProductInWarehouseRequest request);

    public void addQuantity(AddProductToWarehouseRequest request);

    public BookedProductsDto checkQuantity(CartDto cart);

    public AddressDto getWarehouseAddress();

}
