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

public interface WarehouseController {

    @PutMapping
    void addProduct(@RequestBody @NotNull @Valid NewProductInWarehouseRequest request);

    @PostMapping("/add")
    void addQuantity(@RequestBody @NotNull @Valid AddProductToWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody @NotNull @Valid CartDto cartDto);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/assembly")
    BookedProductsDto assemblyProducts(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/shipped")
    void shippedProducts(@RequestBody @Valid ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnProducts(@RequestBody Map<UUID, Long> products);

}
