package ru.yandex.practicum.service;



import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto order);

    BigDecimal getTotalCost(OrderDto order);

    void paymentSuccess(UUID orderId);

    BigDecimal getProductCost(OrderDto order);

    void paymentFailed(UUID orderId);
}
