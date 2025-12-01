package ru.yandex.practicum.shoppingStore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.shoppingStore.enums.QuantityState;

import java.util.UUID;

@Data
public class SetProductQuantityStateRequest {
    @NotNull(message = "Product ID must not be null")
    private final UUID productId;

    @NotNull(message = "Quantity state must not be null")
    private final QuantityState quantityState;
}