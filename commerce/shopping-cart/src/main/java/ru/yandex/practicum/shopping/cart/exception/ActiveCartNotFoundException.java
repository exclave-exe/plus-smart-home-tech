package ru.yandex.practicum.shopping.cart.exception;

public class ActiveCartNotFoundException extends RuntimeException {

    public ActiveCartNotFoundException(String message) {
        super(message);
    }

}
