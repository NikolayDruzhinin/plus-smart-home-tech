package ru.yandex.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.util.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(Request request, final NotFoundException e) {
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
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleConstraintViolationException(
            Request request, final ConstraintViolationException e) throws NotAuthorizedUserException {
        if (e.getConstraintViolations().stream()
                .anyMatch(violation -> violation.getConstraintDescriptor()
                        .getAnnotation().annotationType().equals(NotBlank.class))) {
            return ErrorResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .path(request.getContextPath())
                    .error(e.getClass().getName())
                    .message(e.getMessage())
                    .details(new ErrorResponse
                            .ErrorDetails(ExceptionUtils.getRootCause(e),
                            ExceptionUtils.getStackTrace(e)))
                    .build();
        }

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

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Request request, final Throwable e) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .path(request.getContextPath())
                .error(e.getClass().getName())
                .message(e.getMessage())
                .details(new ErrorResponse
                        .ErrorDetails(ExceptionUtils.getRootCause(e),
                        ExceptionUtils.getStackTrace(e)))
                .build();
    }
}
