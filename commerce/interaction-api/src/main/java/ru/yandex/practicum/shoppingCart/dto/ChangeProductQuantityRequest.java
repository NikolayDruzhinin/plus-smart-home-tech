package ru.yandex.practicum.shoppingCart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ChangeProductQuantityRequest {
    @NotNull(message = "Product ID cannot be null")
    private final UUID productId;

    @NotNull(message = "Quantity cannot be null")
    private final Long newQuantity;
}