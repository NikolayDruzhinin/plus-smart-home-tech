package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class AddProductToWarehouseRequest {
    @NotNull(message = "Product ID must not be null")
    private final UUID productId;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be positive")
    private final Long quantity;
}