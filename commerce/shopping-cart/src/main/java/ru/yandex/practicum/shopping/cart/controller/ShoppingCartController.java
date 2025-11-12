package ru.yandex.practicum.shopping.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.feign.client.cart.CartFeignClient;
import ru.yandex.practicum.shopping.cart.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements CartFeignClient {
    public final CartService cartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        log.info("GET /api/v1/shopping-cart, username = {}", username);
        return cartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductInCart(@RequestParam String username,
                                            @RequestBody @NotEmpty Map<UUID, @NotNull @Positive Integer> products) {
        log.info("PUT /api/v1/shopping-cart, username = {}, newProducts = {}",
                username, products);
        return cartService.addProductInCart(username, products);
    }

    @DeleteMapping
    public void deactivationShoppingCart(@RequestParam String username) {
        log.info("DELETE /api/v1/shopping-cart, username = {}", username);
        cartService.deactivationShoppingCart(username);
    }


    @PostMapping("/remove")
    public ShoppingCartDto removeProductFromCart(@RequestParam String username,
                                                 @RequestBody @NotEmpty List<UUID> productsIds) {
        log.info("POST /api/v1/shopping-cart/remove, productsIds = {}, username = {}",
                productsIds, username);
        return cartService.removeProductFromCart(username, productsIds);
    }

    @PostMapping("change-quantity")
    public ShoppingCartDto changeQuantityInCart(@RequestParam String username,
                                                @Valid @RequestBody ChangeProductQuantityRequest quantityRequest) {
        log.info("POST /api/v1/shopping-cart/change-quantity, username = {}", username);
        return cartService.changeQuantityInCart(username, quantityRequest);
    }
}









