package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotNull
    UUID orderId;
    UUID shoppingCartId;
    @NotEmpty
    Map<UUID, Integer> products;
    UUID paymentId;
    UUID deliveryId;
    OrderStatus state;
    Double deliveryWeight;
    Double deliveryVolume;
    boolean fragile;
    BigDecimal totalPrice;
    BigDecimal deliveryPrice;
    BigDecimal productPrice;
}
