package ru.yandex.practicum.shoppingStore.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.shoppingStore.dto.Pageable;
import ru.yandex.practicum.shoppingStore.dto.ProductDto;
import ru.yandex.practicum.shoppingStore.dto.ProductsDto;
import ru.yandex.practicum.shoppingStore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Validated
@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {
    String BASE_PATH = "";
    String REMOVE_PATH = "/removeProductFromStore";
    String QUANTITY_STATE_PATH = "/quantityState";

    @GetMapping(BASE_PATH)
    ProductsDto getProducts(@RequestParam @Valid ProductCategory category, Pageable pageable);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @PutMapping(BASE_PATH)
    ProductDto createProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping(BASE_PATH)
    ProductDto updateProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping(REMOVE_PATH)
    void removeProduct(@RequestBody UUID productId);

    @PostMapping(QUANTITY_STATE_PATH)
    ProductDto updateQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request);

    @GetMapping("/onlyIds")
    List<ProductDto> getProductByIds(@RequestParam Collection<UUID> ids);
}