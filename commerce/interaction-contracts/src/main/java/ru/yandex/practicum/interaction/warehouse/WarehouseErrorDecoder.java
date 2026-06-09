package ru.yandex.practicum.interaction.warehouse;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartLowQuantityInWarehouse;

public class WarehouseErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        return switch (response.status()) {
            case 400 -> new ProductInShoppingCartLowQuantityInWarehouse("Товар закончился на складе");
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}