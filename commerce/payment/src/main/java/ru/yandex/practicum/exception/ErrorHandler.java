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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoPaymentFoundException(Request request,
                                                       final NoPaymentFoundException e) {
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
    public ErrorResponse handleNotEnoughInfoInOrderToCalculateException(
            Request request, final NotEnoughInfoInOrderToCalculateException e) {
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
