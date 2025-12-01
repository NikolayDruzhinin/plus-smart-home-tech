package ru.yandex.practicum.shoppingStore.dto;

import lombok.Data;

@Data
public class SortInfo {
    private final String property;
    private final String direction;
}