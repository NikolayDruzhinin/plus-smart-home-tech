package ru.yandex.practicum.shoppingCart.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.shoppingCart.dto.BookedProductsDto;
import ru.yandex.practicum.shoppingCart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingCart.dto.ShoppingCartDto;
import ru.yandex.practicum.utils.ValidationUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient {
    String USERNAME_PARAM = "username";
    String VALIDATION_MESSAGE = ValidationUtil.VALIDATION_USERNAME_MESSAGE;
    String REMOVE_PATH = "/remove";
    String CHANGE_QUANTITY_PATH = "/change-quantity";
    String BOOKING_PATH = "/booking";

    @GetMapping
    ShoppingCartDto getCart(@RequestParam(USERNAME_PARAM) @NotBlank(message = VALIDATION_MESSAGE) String userName);

    @PutMapping
    ShoppingCartDto addProducts(@RequestParam(USERNAME_PARAM) @NotBlank(message = VALIDATION_MESSAGE) String userName,
                                @RequestBody Map<UUID, Long> products);

    @DeleteMapping
    void clearCart(@RequestParam(USERNAME_PARAM) @NotBlank(message = VALIDATION_MESSAGE) String userName);

    @PostMapping(REMOVE_PATH)
    ShoppingCartDto removeProducts(@RequestParam(USERNAME_PARAM) @NotBlank(message = VALIDATION_MESSAGE) String userName,
                                   @RequestBody List<UUID> products);

    @PostMapping(CHANGE_QUANTITY_PATH)
    ShoppingCartDto updateQuantity(@RequestParam(USERNAME_PARAM) @NotBlank(message = VALIDATION_MESSAGE) String userName,
                                   @RequestBody @Valid ChangeProductQuantityRequest request);

    @PostMapping(BOOKING_PATH)
    BookedProductsDto bookProducts(@RequestParam(USERNAME_PARAM) @NotBlank(message = VALIDATION_MESSAGE) String userName);
}