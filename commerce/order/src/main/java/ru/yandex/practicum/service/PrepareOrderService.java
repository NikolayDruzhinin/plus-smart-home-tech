package ru.yandex.practicum.service;

import ru.yandex.practicum.order.dto.OrderCreateRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface PrepareOrderService {
    List<OrderDto> getClientOrders(String userName);

    OrderDto createNewOrder(OrderCreateRequest request);

    OrderDto returnProducts(ProductReturnRequest request);

    OrderDto payOrder(UUID orderId);

    OrderDto successPayOrder(UUID orderId);

    OrderDto failPayOrder(UUID orderId);

    OrderDto deliverOrder(UUID orderId);

    OrderDto failDeliverOrder(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto failAssemblyOrder(UUID orderId);

    OrderDto getOrderDetails(UUID orderId);
}
