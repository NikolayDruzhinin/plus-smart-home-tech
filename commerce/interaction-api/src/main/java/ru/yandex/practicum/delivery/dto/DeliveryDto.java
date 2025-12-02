package ru.yandex.practicum.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.delivery.enums.DeliveryStatus;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import java.util.UUID;

@Builder
@Getter
public class DeliveryDto {
    private final UUID deliveryId;
    @NotNull
    private final AddressDto senderAddress;
    @NotNull
    private final AddressDto recipientAddress;
    @NotNull
    private final UUID orderId;
    @NotNull
    private final DeliveryStatus deliveryStatus;
}
