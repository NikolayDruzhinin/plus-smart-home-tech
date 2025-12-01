package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;
import ru.yandex.practicum.shoppingStore.enums.ProductState;
import ru.yandex.practicum.shoppingStore.enums.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID productId;

    @Column(name = "name")
    private final String productName;
    private final String description;
    private final String imageSrc;

    @Enumerated(value = EnumType.STRING)
    private QuantityState quantityState;

    @Enumerated(value = EnumType.STRING)
    private ProductState productState;
    private final Double rating;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category")
    private final ProductCategory productCategory;

    private final Double price;
}
