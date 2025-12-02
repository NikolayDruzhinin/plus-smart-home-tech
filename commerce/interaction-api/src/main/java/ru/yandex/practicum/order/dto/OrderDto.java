package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.order.enums.OrderStatus;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
public class OrderDto {
    @NotNull
    private final UUID orderId;
    private final UUID shoppingCartId;
    @NotEmpty
    private final Map<UUID, Integer> products;
    private final UUID paymentId;
    private final UUID deliveryId;
    private final OrderStatus state;
    private final Double deliveryWeight;
    private final Double deliveryVolume;
    private final boolean fragile;
    private final Double totalPrice;
    private final Double deliveryPrice;
    private final Double productPrice;
}
