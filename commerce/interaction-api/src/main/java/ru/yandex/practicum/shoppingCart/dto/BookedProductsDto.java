package ru.yandex.practicum.shoppingCart.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class BookedProductsDto {
    @PositiveOrZero(message = "Delivery weight must be positive or zero")
    private final Double deliveryWeight;

    @PositiveOrZero(message = "Delivery volume must be positive or zero")
    private final Double deliveryVolume;

    private final Boolean fragile;
}