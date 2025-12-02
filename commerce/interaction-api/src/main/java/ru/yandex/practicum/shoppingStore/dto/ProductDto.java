package ru.yandex.practicum.shoppingStore.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;
import ru.yandex.practicum.shoppingStore.enums.ProductState;
import ru.yandex.practicum.shoppingStore.enums.QuantityState;

import java.util.UUID;

@Getter
@Builder
public class ProductDto {
    private final UUID productId;

    @NotBlank(message = "Product name cannot be blank")
    private final String productName;

    @NotBlank(message = "Description cannot be blank")
    private final String description;

    private final String imageSrc;

    @NotNull(message = "Quantity state must be specified")
    @Enumerated(value = EnumType.STRING)
    private final QuantityState quantityState;

    @NotNull(message = "Product state must be specified")
    @Enumerated(value = EnumType.STRING)
    private final ProductState productState;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5")
    private final Double rating;

    @NotNull(message = "Category must be specified 1.2.643.5.1.13.13.12.2.47")
    @Enumerated(value = EnumType.STRING)
    private final ProductCategory productCategory;

    @NotNull(message = "Price must be specified")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private final Double price;
}