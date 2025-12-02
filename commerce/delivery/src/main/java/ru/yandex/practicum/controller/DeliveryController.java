package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.delivery.client.DeliveryClient;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;


@Validated
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @Override
    @PutMapping
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @Override
    @PostMapping("/successful")
    public void completeDelivery(UUID orderId) {
        deliveryService.completeDelivery(orderId);
    }

    @Override
    @PostMapping("/picked")
    public void confirmPickup(UUID orderId) {
        deliveryService.confirmPickup(orderId);
    }

    @Override
    @PostMapping("/failed")
    public void failDelivery(UUID orderId) {
        deliveryService.failDelivery(orderId);
    }

    @Override
    @PostMapping("/cost")
    public Double calculateDeliveryCost(OrderDto orderDto) {
        return deliveryService.calculateDeliveryCost(orderDto);
    }
}
