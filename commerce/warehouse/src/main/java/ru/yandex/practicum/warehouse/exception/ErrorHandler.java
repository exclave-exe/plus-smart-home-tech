package ru.yandex.practicum.warehouse.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartLowQuantityInWarehouse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProduct(NoSpecifiedProductInWarehouseException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLowQuantity(ProductInShoppingCartLowQuantityInWarehouse e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAlreadyExists(SpecifiedProductAlreadyInWarehouseException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentTypeMismatchException e) {
        return new ErrorResponse("Ошибка приведения параметра запроса");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ConstraintViolationException e) {
        return new ErrorResponse("Ошибка валидации параметров запроса");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        return new ErrorResponse("Ошибка валидации тела запроса");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse("Внутренняя ошибка сервиса");
    }

}
