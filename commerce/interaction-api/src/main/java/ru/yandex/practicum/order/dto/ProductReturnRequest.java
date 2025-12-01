package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductReturnRequest {
    @NotNull
    private final UUID orderId;
    @NotEmpty
    private final Map<UUID, Integer> products;
}
