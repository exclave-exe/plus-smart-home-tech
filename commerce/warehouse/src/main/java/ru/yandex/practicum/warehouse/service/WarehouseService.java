package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.delivery.dto.AddressDto;
import ru.yandex.practicum.interaction.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    public void addNewProduct(NewProductInWarehouseRequest request);

    public void addQuantity(AddProductToWarehouseRequest request);

    public BookedProductsDto checkQuantity(CartDto cart);

    public AddressDto getWarehouseAddress();

    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request);

    public void shippedProducts(ShippedToDeliveryRequest request);

    public void returnProducts(Map<UUID, Long> products);

}
