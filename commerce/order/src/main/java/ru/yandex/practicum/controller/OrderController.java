package ru.yandex.practicum.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.PrepareOrderService;
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderCreateRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {
    private final PrepareOrderService prepareOrderService;

    @Override
    @GetMapping
    public List<OrderDto> getClientOrders(String userName) {
        return prepareOrderService.getClientOrders(userName);
    }

    @Override
    @PutMapping
    public OrderDto createNewOrder(OrderCreateRequest request) {
        return prepareOrderService.createNewOrder(request);
    }

    @Override
    @PostMapping("/return")
    public OrderDto returnProducts(ProductReturnRequest request) {
        return prepareOrderService.returnProducts(request);
    }

    @Override
    @PostMapping("/payment")
    public OrderDto payOrder(UUID orderId) {
        return prepareOrderService.payOrder(orderId);
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto failPayOrder(UUID orderId) {
        return prepareOrderService.failPayOrder(orderId);
    }

    @Override
    @PostMapping("/payment/success")
    public OrderDto successPayOrder(UUID orderId) {
        return prepareOrderService.successPayOrder(orderId);
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto deliverOrder(UUID orderId) {
        return prepareOrderService.deliverOrder(orderId);
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto failDeliverOrder(UUID orderId) {
        return prepareOrderService.failDeliverOrder(orderId);
    }

    @Override
    @PostMapping("/completed")
    public OrderDto completeOrder(UUID orderId) {
        return prepareOrderService.completeOrder(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPrice(UUID orderId) {
        return prepareOrderService.calculateTotalPrice(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        return prepareOrderService.calculateDeliveryPrice(orderId);
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto assemblyOrder(UUID orderId) {
        return prepareOrderService.assemblyOrder(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto failAssemblyOrder(UUID orderId) {
        return prepareOrderService.failAssemblyOrder(orderId);
    }

    @Override
    @GetMapping("/only")
    public OrderDto getOrder(@RequestBody @NotNull UUID orderId) {
        return prepareOrderService.getOrderDetails(orderId);
    }
}
