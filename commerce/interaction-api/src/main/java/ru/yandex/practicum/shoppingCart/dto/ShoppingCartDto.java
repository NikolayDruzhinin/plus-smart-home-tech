package ru.yandex.practicum.shoppingCart.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ShoppingCartDto {
    private final UUID id;

    @NotEmpty(message = "Products map cannot be empty")
    private final Map<UUID, Long> products;
}