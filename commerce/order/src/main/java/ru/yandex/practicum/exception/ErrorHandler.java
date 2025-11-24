package ru.yandex.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("Not found exception occurred: {}", e.getMessage(), e);

        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                "Resource not found"
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException e) throws NotAuthorizedUserException {
        if (e.getConstraintViolations().stream()
                .anyMatch(violation -> violation.getConstraintDescriptor()
                        .getAnnotation().annotationType().equals(NotBlank.class))) {
            log.error("Unauthorized exception occurred: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.UNAUTHORIZED,
                            e.getMessage(),
                            "Unauthorized"
                    ), HttpStatus.UNAUTHORIZED
            );
        }

        log.error("Bad Request exception occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        "Bad Request"
                        ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                "Internal server error"
        );
    }
}
