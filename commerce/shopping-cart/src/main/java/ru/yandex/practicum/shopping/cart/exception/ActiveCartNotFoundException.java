package ru.yandex.practicum.shopping.cart.exception;

public class CartDeactivatedException extends RuntimeException {
  public CartDeactivatedException(String message) {
    super(message);
  }
}
