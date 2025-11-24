package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AssemblyRequest {
    @NotNull
    UUID orderId;
    @NotEmpty
    Map<UUID, Long> products;
}
