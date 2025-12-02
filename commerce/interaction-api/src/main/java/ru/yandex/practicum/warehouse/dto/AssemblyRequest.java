package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class AssemblyRequest {
    @NotNull
    private final UUID orderId;
    @NotEmpty
    private final Map<UUID, Long> products;
}
