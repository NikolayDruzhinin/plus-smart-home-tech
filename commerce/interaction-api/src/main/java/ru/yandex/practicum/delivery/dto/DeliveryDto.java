package ru.yandex.practicum.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.delivery.enums.DeliveryStatus;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DeliveryDto {
    UUID deliveryId;
    @NotNull
    AddressDto senderAddress;
    @NotNull
    AddressDto recipientAddress;
    @NotNull
    UUID orderId;
    @NotNull
    DeliveryStatus deliveryStatus;
}
