package ru.yandex.practicum.delivery.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.order.dto.OrderDto;


import java.math.BigDecimal;
import java.util.UUID;

@Validated
@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void completeDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/picked")
    void confirmPickup(@RequestBody @NotNull UUID orderId);

    @PostMapping("/failed")
    void failDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/cost")
    BigDecimal calculateDeliveryCost(@RequestBody @Valid OrderDto orderDto);
}
