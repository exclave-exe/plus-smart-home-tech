package ru.yandex.practicum.interaction.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.delivery.dto.AddressDto;
import ru.yandex.practicum.interaction.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseOperations {

    // Добавить новый товар на склад
    @PutMapping
    void addProduct(@RequestBody @NotNull @Valid NewProductInWarehouseRequest request);

    // Принять товар на склад.
    @PostMapping("/add")
    void addQuantity(@RequestBody @NotNull @Valid AddProductToWarehouseRequest request);

    // Проверить что количество товаров на складе достаточно
    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody @NotNull @Valid CartDto cartDto);

    // Предоставить адрес склада для расчёта доставки
    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    // Собрать товары к заказу для подготовки к отправке.
    @PostMapping("/assembly")
    BookedProductsDto assemblyProducts(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    // Передать товары в доставку
    @PostMapping("/shipped")
    void shippedProducts(@RequestBody @Valid ShippedToDeliveryRequest request);

    // Принять возврат товаров на склад
    @PostMapping("/return")
    void returnProducts(@RequestBody Map<UUID, Long> products);

}
