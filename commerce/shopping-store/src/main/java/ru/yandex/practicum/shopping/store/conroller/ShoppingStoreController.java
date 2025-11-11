package ru.yandex.practicum.shopping.store.conroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.dto.store.ProductDto;
import ru.yandex.practicum.interaction.api.dto.store.ProductPageDto;
import ru.yandex.practicum.interaction.api.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.api.enums.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.QuantityState;
import ru.yandex.practicum.interaction.api.feign.client.store.StoreFeignClient;
import ru.yandex.practicum.shopping.store.service.StoreService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController implements StoreFeignClient {
    private final StoreService storeService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("GET /api/v1/shopping-store, category = {}, pageable = {}", category, pageable);
        return storeService.getAllProducts(category, pageable).getContent();
    }

    @PutMapping
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("PUT /api/v1/shopping-store, productName = {}",
                productDto.getProductName());
        return storeService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("POST /api/v1/shopping-store, productName = {}",
                productDto.getProductName());
        return storeService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean removeProductById(@RequestBody UUID productId) {
        log.info("POST /api/v1/shopping-store/removeProductFromStore, ID = {}", productId);
        return storeService.removeProductById(productId);
    }

    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(@RequestParam UUID productId,
                                           @RequestParam QuantityState quantityState) {
        log.info("POST /api/v1/shopping-store/quantityState, ID = {}, quantityState = {}",
                productId, quantityState);
        SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build();

        return storeService.setProductQuantityState(request);
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable UUID productId) {
        log.info("GET /api/v1/shopping-store/{}", productId);
        return storeService.getProductById(productId);
    }
}