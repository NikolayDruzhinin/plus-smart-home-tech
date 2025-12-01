package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.shoppingCart.dto.ShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.AddressDto;

@Data
@AllArgsConstructor
public class OrderCreateRequest {
    @NotBlank
    private final String userName;
    @NotNull
    private final ShoppingCartDto shoppingCart;
    @NotNull
    private final AddressDto recipientAddress;
}
