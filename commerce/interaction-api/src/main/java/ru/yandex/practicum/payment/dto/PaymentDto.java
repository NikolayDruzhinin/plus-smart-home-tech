package ru.yandex.practicum.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentDto {
    @NotNull
    private final UUID paymentId;
    private final Double totalPayment;
    private final Double deliveryTotal;
    private final Double feeTotal;
}
