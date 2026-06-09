package ru.yandex.practicum.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.warehouse.WarehouseClient;
import ru.yandex.practicum.shopping.cart.exception.NoProductsInCartException;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NonTransactionalCartServiceImpl implements NonTransactionalCartService {

    private final WarehouseClient warehouseClient;
    private final TransactionalCartService transactionalCartService;

    @Override
    public CartDto addProductsToCart(String username, Map<UUID, Long> products) {
        CartDto currentCartDto = transactionalCartService.getCart(username);
        products.forEach((productId, quantity) -> {
            currentCartDto.getProducts().merge(productId, quantity, Long::sum);
        });

        warehouseClient.checkQuantity(currentCartDto);
        return transactionalCartService.addProductsToCart(username, products);
    }

    @Override
    public CartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        CartDto currentCartDto = transactionalCartService.getCart(username);

        if (!currentCartDto.getProducts().containsKey(request.getProductId())) {
            throw new NoProductsInCartException("Товар не найден в корзине: " + request.getProductId());
        }

        if (request.getNewQuantity() == 0) {
            currentCartDto.getProducts().remove(request.getProductId());
        } else {
            currentCartDto.getProducts().put(request.getProductId(), request.getNewQuantity());
        }

        warehouseClient.checkQuantity(currentCartDto);
        return transactionalCartService.changeProductQuantity(username, request);
    }
}
