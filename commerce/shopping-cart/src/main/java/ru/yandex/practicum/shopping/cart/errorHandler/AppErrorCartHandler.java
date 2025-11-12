package ru.yandex.practicum.shopping.cart.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.interaction.api.exception.AppError;
import ru.yandex.practicum.interaction.api.exception.cart.CartNotFoundException;
import ru.yandex.practicum.interaction.api.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.api.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.api.exception.cart.ShoppingCartDeactivateException;

@Slf4j
@RestControllerAdvice
public class AppErrorCartHandler {

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleNoProductsInShoppingCart(NoProductsInShoppingCartException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("Products not found in cart " + exp.getMessage());
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AppError handleNotAuthorizedUser(NotAuthorizedUserException exp) {
        return new AppError("User not authorized: " + exp.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError handleCartNotFound(CartNotFoundException exp) {
        return new AppError("Cart not found: " + exp.getMessage());
    }

    @ExceptionHandler(ShoppingCartDeactivateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleShoppingCartDeactivate(ShoppingCartDeactivateException exp) {
        return new AppError("Cart was deactivated: " + exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException exp) {
        return new AppError("Validation error" + exp.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleConstraintViolations(ConstraintViolationException exp) {
        return new AppError("Constraint violation: " + exp.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(Throwable exp) {
        return new AppError("Internal server error: " + exp.getMessage());
    }
}
