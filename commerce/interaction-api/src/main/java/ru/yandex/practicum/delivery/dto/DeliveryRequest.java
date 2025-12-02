package ru.yandex.practicum.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DeliveryRequest {
    @NotNull
    private final UUID orderId;
    @NotNull
    private final UUID deliveryId;
}
