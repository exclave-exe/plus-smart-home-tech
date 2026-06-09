package ru.yandex.practicum.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.shopping.cart.exception.ActiveCartNotFoundException;
import ru.yandex.practicum.shopping.cart.exception.NoProductsInCartException;
import ru.yandex.practicum.shopping.cart.mapper.CartMapper;
import ru.yandex.practicum.shopping.cart.model.Cart;
import ru.yandex.practicum.shopping.cart.repository.CartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository repository;
    private final CartMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CartDto getCart(String username) {
        Cart cart = getActiveCartOrCreateNewCart(username);
        return mapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addProductsToCart(String username, Map<UUID, Long> products) {
        Cart cart = getActiveCartOrCreateNewCart(username);

        products.forEach((productId, quantity) -> {
            cart.getProducts().merge(productId, quantity, Long::sum);
        });

        repository.save(cart);
        return mapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        Cart cart = getActiveCart(username);

        if (!cart.getProducts().containsKey(request.getProductId())) {
            throw new NoProductsInCartException("Товар не найден в корзине: " + request.getProductId());
        }

        if (request.getNewQuantity() == 0) {
            cart.getProducts().remove(request.getProductId());
        } else {
            cart.getProducts().put(request.getProductId(), request.getNewQuantity());
        }

        repository.save(cart);
        return mapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto removeProductsFromCart(String username, List<UUID> productIds) {
        Cart cart = getActiveCart(username);

        productIds.forEach(productId -> {
            if (!cart.getProducts().containsKey(productId)) {
                throw new NoProductsInCartException("Товар не найден в корзине: " + productId);
            }
            cart.getProducts().remove(productId);
        });

        repository.save(cart);
        return mapper.toDto(cart);
    }

    @Override
    @Transactional
    public void deactivateCart(String username) {
        repository.findByUsernameAndActiveTrue(username).ifPresent(cart -> {
            cart.setActive(false);
            repository.save(cart);
        });
    }

    private Cart getActiveCart(String username) {
        return repository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new ActiveCartNotFoundException("Активная корзина не найдена: " + username));
    }

    private Cart getActiveCartOrCreateNewCart(String username) {
        return repository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> createNewCart(username));
    }

    private Cart createNewCart(String username) {
        Cart cart = new Cart();
        cart.setUsername(username);
        cart.setActive(true);
        cart.setProducts(new HashMap<>());
        return repository.save(cart);
    }

}