package ru.practicum.collector.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceAction(@NotBlank String sensorId, @NotNull ActionType type, Integer value) {
}
