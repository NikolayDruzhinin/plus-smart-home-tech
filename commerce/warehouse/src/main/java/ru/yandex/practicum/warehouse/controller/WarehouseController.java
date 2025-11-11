package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.api.feign.client.warehouse.WarehouseFeignClient;
import ru.yandex.practicum.warehouse.service.WarehouseService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseFeignClient {
    private final WarehouseService warehouseService;

    @PutMapping
    public void newProduct(@Valid @RequestBody NewProductInWarehouseRequest newRequest) {
        log.debug("Adding new product {}", newRequest);
        warehouseService.newProduct(newRequest);
    }

    @PostMapping("/check")
    public BookedProductsDto checkQuantityProducts(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.debug("Checking product count {}", shoppingCartDto);
        return warehouseService.checkQuantityProducts(shoppingCartDto);
    }

    @PostMapping("/add")
    public void addQuantityProduct(@Valid @RequestBody AddProductToWarehouseRequest addRequest) {
        log.info("Taking goods {}", addRequest);
        warehouseService.addQuantityProduct(addRequest);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        log.info("Request warehouse address");
        return warehouseService.getAddress();
    }
}











