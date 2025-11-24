package ru.yandex.practicum.shoppingCart.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookedProductsDto {
    @PositiveOrZero(message = "Delivery weight must be positive or zero")
    Double deliveryWeight;
    
    @PositiveOrZero(message = "Delivery volume must be positive or zero")
    Double deliveryVolume;
    
    Boolean fragile;
}