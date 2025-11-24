package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.delivery.client.DeliveryClient;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.delivery.enums.DeliveryStatus;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.dto.OrderCreateRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;
import ru.yandex.practicum.order.enums.OrderStatus;
import ru.yandex.practicum.payment.client.PaymentClient;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.shoppingCart.client.ShoppingCartClient;
import ru.yandex.practicum.shoppingCart.dto.BookedProductsDto;
import ru.yandex.practicum.warehouse.client.WarehouseClient;
import ru.yandex.practicum.warehouse.dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.AssemblyRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrepareOrderServiceImpl implements PrepareOrderService {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    private final ShoppingCartClient shoppingCartClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;

    @Override
    public List<OrderDto> getClientOrders(String userName) {
        log.info("get orders for user {}", userName);
        return orderService.getClientOrders(userName)
                .stream()
                .map(orderMapper::map)
                .toList();
    }

    @Override
    public OrderDto createNewOrder(OrderCreateRequest request) {
        Order order = orderService.createNewOrder(getNewOrderFromRequest(request));
        log.info("new order from request: {}", order);

        UUID deliveryId = getNewDeliveryId(order.getOrderId(), request.getRecipientAddress());
        return orderMapper.map(orderService.setDelivery(order.getOrderId(), deliveryId));
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest request) {
        warehouseClient.acceptReturn(request.getProducts());

        return orderMapper.map(orderService.returnProducts(request.getOrderId()));
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        BigDecimal productCost = paymentClient.getProductCost(orderMapper.map(order));
        BigDecimal deliveryCost = deliveryClient.calculateDeliveryCost(orderMapper.map(order));
        order.setDeliveryPrice(deliveryCost);
        order.setProductPrice(productCost);
        log.info("order after setting productPrice: {}", order);
        BigDecimal totalCost = paymentClient.getTotalCost(orderMapper.map(order));
        order.setTotalPrice(totalCost);
        PaymentDto paymentDto = paymentClient.createPayment(orderMapper.map(order));
        order.setPaymentId(paymentDto.getPaymentId());

        Order savedOrder = orderService.savePaymentInfo(order);
        log.info("payOrder: order after creating payment {}", savedOrder);
        return orderMapper.map(savedOrder);
    }

    @Override
    public OrderDto successPayOrder(UUID orderId) {
        return orderMapper.map(orderService.successPayOrder(orderId));
    }

    @Override
    public OrderDto failPayOrder(UUID orderId) {
        return orderMapper.map(orderService.failPayOrder(orderId));
    }

    @Override
    public OrderDto deliverOrder(UUID orderId) {
        return orderMapper.map(orderService.deliverOrder(orderId));
    }

    @Override
    public OrderDto failDeliverOrder(UUID orderId) {
        return orderMapper.map(orderService.failDeliverOrder(orderId));
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        return orderMapper.map(orderService.completeOrder(orderId));
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) {
        Order order = getOrderById(orderId);
        BigDecimal totalCost = paymentClient.getTotalCost(orderMapper.map(order));

        return orderMapper.map(orderService.setTotalPrice(orderId, totalCost));
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Order order = getOrderById(orderId);
        BigDecimal deliveryCost = deliveryClient.calculateDeliveryCost(orderMapper.map(order));

        return orderMapper.map(orderService.setDeliveryPrice(orderId, deliveryCost));
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        warehouseClient.assemblyProductsForOrder(getNewAssemblyProductsForOrderRequest(orderId));

        return orderMapper.map(orderService.assemblyOrder(orderId));
    }

    @Override
    public OrderDto failAssemblyOrder(UUID orderId) {
        return orderMapper.map(orderService.failAssemblyOrder(orderId));
    }

    @Override
    public OrderDto getOrderDetails(UUID orderId) {
        return orderMapper.map(getOrderById(orderId));
    }

    private AssemblyRequest getNewAssemblyProductsForOrderRequest(UUID orderId) {
        Order order = getOrderById(orderId);
        return AssemblyRequest.builder()
                .orderId(orderId)
                .products(order.getProducts())
                .build();
    }

    private Order getOrderById(UUID orderId) {
        return orderService.getOrderById(orderId);
    }

    private Order getNewOrderFromRequest(OrderCreateRequest request) {
        BookedProductsDto bookedProductsDto = shoppingCartClient.bookProducts(request.getUserName());

        return Order.builder()
                .userName(request.getUserName())
                .cartId(request.getShoppingCart().getId())
                .products(request.getShoppingCart().getProducts())
                .deliveryWeight(bookedProductsDto.getDeliveryWeight())
                .deliveryVolume(bookedProductsDto.getDeliveryVolume())
                .fragile(bookedProductsDto.getFragile())
                .orderStatus(OrderStatus.NEW)
                .build();
    }

    private UUID getNewDeliveryId(UUID orderId, AddressDto deliveryAddress) {
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .senderAddress(warehouseClient.getWarehouseAddress())
                .recipientAddress(deliveryAddress)
                .orderId(orderId)
                .deliveryStatus(DeliveryStatus.CREATED)
                .build();

        log.info("Creating DeliveryDto: {}", deliveryDto);
        return deliveryClient.createDelivery(deliveryDto).getDeliveryId();
    }
}
