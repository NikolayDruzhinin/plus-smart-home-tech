package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {
    @NotNull(message = "Width must be specified")
    @Positive(message = "Width must be positive")
    private Double width;
    
    @NotNull(message = "Height must be specified")
    @Positive(message = "Height must be positive")
    private Double height;
    
    @NotNull(message = "Depth must be specified")
    @Positive(message = "Depth must be positive")
    private Double depth;
}