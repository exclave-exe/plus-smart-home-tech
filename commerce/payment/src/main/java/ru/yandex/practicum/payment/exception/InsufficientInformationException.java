package ru.yandex.practicum.payment.exception;

public class InsufficientInformationException extends RuntimeException {
  public InsufficientInformationException(String message) {
    super(message);
  }
}
