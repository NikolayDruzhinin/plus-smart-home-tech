package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDto {
    @NotBlank
    private final String country;
    @NotBlank
    private final String city;
    @NotBlank
    private final String street;
    @NotBlank
    private final String house;
    @NotBlank
    private final String flat;
}
