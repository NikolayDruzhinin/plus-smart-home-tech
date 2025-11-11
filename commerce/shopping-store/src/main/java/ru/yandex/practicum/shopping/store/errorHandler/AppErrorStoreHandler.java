package ru.yandex.practicum.shopping.store.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.api.exception.AppError;
import ru.yandex.practicum.interaction.api.exception.store.ProductNotFoundException;

@Slf4j
@RestControllerAdvice
public class AppErrorStoreHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError handleProductNotFoundException(ProductNotFoundException exp) {
        return new AppError("Product not found error: " + exp.getMessage(), exp.getCause());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException exp) {
        return new AppError("Validation error: " + exp.getMessage(), exp.getCause());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleConstraintViolations(ConstraintViolationException exp) {
        return new AppError("Constraint violation: " + exp.getMessage(), exp.getCause());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(Throwable exp) {
        return new AppError("Internal server error: " + exp.getMessage(), exp.getCause());
    }
}
