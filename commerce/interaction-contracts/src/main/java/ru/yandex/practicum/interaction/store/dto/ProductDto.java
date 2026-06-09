package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.interaction.enums.ProductCategory;
import ru.yandex.practicum.interaction.enums.ProductState;
import ru.yandex.practicum.interaction.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
public class ProductDto {
    private UUID productId;

    @NotBlank(message = "Наименование товара не может быть пустым")
    private String productName;

    @NotBlank(message = "Описание товара не может быть пустым")
    private String description;

    private String imageSrc;

    @NotNull(message = "Статус остатка обязателен для заполнения")
    private QuantityState quantityState;

    @NotNull(message = "Статус товара обязателен для заполнения")
    private ProductState productState;

    @NotNull(message = "Категория товара обязательна для заполнения")
    private ProductCategory productCategory;

    @NotNull(message = "Цена обязательна для заполнения")
    @DecimalMin(value = "1.0", message = "Цена должна быть не менее 1")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;
}
