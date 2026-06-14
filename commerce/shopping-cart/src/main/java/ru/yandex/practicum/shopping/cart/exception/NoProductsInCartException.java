package ru.yandex.practicum.shopping.cart.exception;

public class NoProductsInCartException extends RuntimeException {

    public NoProductsInCartException(String message) {
        super(message);
    }

}
