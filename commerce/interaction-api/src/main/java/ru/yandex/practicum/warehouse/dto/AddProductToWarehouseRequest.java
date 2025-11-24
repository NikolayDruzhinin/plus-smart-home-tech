package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToWarehouseRequest {
    @NotNull(message = "Product ID must not be null")
    private UUID productId;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be positive")
    private Long quantity;
}