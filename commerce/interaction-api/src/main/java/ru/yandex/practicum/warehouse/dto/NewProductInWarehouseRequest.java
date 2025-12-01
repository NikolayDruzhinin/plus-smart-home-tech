package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;


@Data
public class NewProductInWarehouseRequest {
    @NotNull(message = "Product ID is required")
    private final UUID productId;

    private final Boolean fragile;

    @NotNull(message = "Dimensions are required")
    private final DimensionDto dimension;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 kg")
    private final Double weight;
}