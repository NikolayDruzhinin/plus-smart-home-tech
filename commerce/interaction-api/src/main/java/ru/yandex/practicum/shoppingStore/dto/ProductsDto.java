package ru.yandex.practicum.shoppingStore.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductsDto {
    private final List<ProductDto> content;
    private final List<SortInfo> sort;
}
