package ru.yandex.practicum.interaction.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.warehouse.dto.NewProductInWarehouseRequest;

public interface WarehouseOperations {

    @PutMapping
    void addProduct(@RequestBody @NotNull @Valid NewProductInWarehouseRequest request);

    @PostMapping("/add")
    void addQuantity(@RequestBody @NotNull @Valid AddProductToWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody @NotNull @Valid CartDto cartDto);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

}
