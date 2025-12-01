package ru.yandex.practicum.exception;

import org.apache.catalina.connector.Request;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.util.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpecifiedProductAlreadyInWarehouseException(
            Request request, final SpecifiedProductAlreadyInWarehouseException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .path(request.getContextPath())
                .error(e.getClass().getName())
                .message(e.getMessage())
                .details(new ErrorResponse
                        .ErrorDetails(ExceptionUtils.getRootCause(e),
                        ExceptionUtils.getStackTrace(e)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProductInWarehouseException(
            Request request, final NoSpecifiedProductInWarehouseException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .path(request.getContextPath())
                .error(e.getClass().getName())
                .message(e.getMessage())
                .details(new ErrorResponse
                        .ErrorDetails(ExceptionUtils.getRootCause(e),
                        ExceptionUtils.getStackTrace(e)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartLowQuantityInWarehouse(
            Request request, final ProductInShoppingCartLowQuantityInWarehouse e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .path(request.getContextPath())
                .error(e.getClass().getName())
                .message(e.getMessage())
                .details(new ErrorResponse
                        .ErrorDetails(ExceptionUtils.getRootCause(e),
                        ExceptionUtils.getStackTrace(e)))
                .build();
    }
}
