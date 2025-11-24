package ru.yandex.practicum.shoppingStore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.shoppingStore.enums.QuantityState;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateRequest {
    @NotNull(message = "Product ID must not be null")
    private UUID productId;
    
    @NotNull(message = "Quantity state must not be null")
    private QuantityState quantityState;
}