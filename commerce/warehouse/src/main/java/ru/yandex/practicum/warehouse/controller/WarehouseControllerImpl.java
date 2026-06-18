package ru.yandex.practicum.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.delivery.dto.AddressDto;
import ru.yandex.practicum.interaction.warehouse.WarehouseController;
import ru.yandex.practicum.interaction.warehouse.dto.*;
import ru.yandex.practicum.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseControllerImpl implements WarehouseController {

    private final WarehouseService warehouseService;

    // Добавить новый товар на склад
    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        warehouseService.addNewProduct(request);
    }

    // Принять товар на склад.
    @Override
    public void addQuantity(AddProductToWarehouseRequest request) {
        warehouseService.addQuantity(request);
    }

    // Проверить что количество товаров на складе достаточно
    @Override
    public BookedProductsDto checkQuantity(CartDto cartDto) {
        return warehouseService.checkQuantity(cartDto);
    }

    // Предоставить адрес склада для расчёта доставки
    @Override
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }

    // Собрать товары к заказу для подготовки к отправке.
    @Override
    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyProducts(request);
    }

    // Передать товары в доставку
    @Override
    public void shippedProducts(ShippedToDeliveryRequest request) {
        warehouseService.shippedProducts(request);
    }

    // Принять возврат товаров на склад
    @Override
    public void returnProducts(Map<UUID, Long> products) {
        warehouseService.returnProducts(products);
    }

}