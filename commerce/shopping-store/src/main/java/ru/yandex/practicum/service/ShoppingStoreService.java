package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.shoppingStore.dto.Pageable;
import ru.yandex.practicum.shoppingStore.dto.ProductDto;
import ru.yandex.practicum.shoppingStore.dto.ProductsDto;
import ru.yandex.practicum.shoppingStore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShoppingStoreService {

    ProductsDto getProducts(ProductCategory category, Pageable pageable);

    ProductDto getProductById(UUID productId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto updateQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request);

    void removeProduct(UUID productId);

    List<ProductDto> getProductByIds(Collection<UUID> ids);
}
