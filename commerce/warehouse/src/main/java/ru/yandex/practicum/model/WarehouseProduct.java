package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "warehouse_product")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class WarehouseProduct {
    @Id
    @Column(name = "product_id")
    private final UUID productId;
    private final boolean fragile;
    private final Double width;
    private final Double height;
    private final Double depth;
    private final Double weight;

    @Builder.Default
    private long quantity = 0L;
}
