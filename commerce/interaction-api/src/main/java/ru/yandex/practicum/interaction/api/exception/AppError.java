package ru.yandex.practicum.interaction.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppError {
    private String message;

    public AppError(String message, Throwable cause) {
    }
}
