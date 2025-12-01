package ru.yandex.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.catalina.connector.Request;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.util.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUserException(Request request, NotAuthorizedException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .path(request.getContextPath())
                .message(e.getMessage())
                .error(e.getClass().getName())
                .details(new ErrorResponse
                        .ErrorDetails(ExceptionUtils.getRootCause(e),
                        ExceptionUtils.getStackTrace(e)))
                .build();

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(Request request, NotFoundException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
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
    public ErrorResponse handleNoProductsInShoppingCartException(Request request,
                                                                 NoProductsException e) {
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
    public ErrorResponse handleConstraintViolationException(Request request,
                                                            ConstraintViolationException e) {
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
