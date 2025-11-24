package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.enums.OrderStatus;
import ru.yandex.practicum.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public List<Order> getClientOrders(String userName) {
        log.info("get orders for user {}", userName);
        return orderRepository.findAllByUserName(userName);
    }

    @Override
    @Transactional
    public Order createNewOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order returnProducts(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.PRODUCT_RETURNED);
    }

    @Override
    @Transactional
    public Order successPayOrder(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.PAID);
    }

    @Override
    @Transactional
    public Order failPayOrder(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.PAYMENT_FAILED);
    }

    @Override
    @Transactional
    public Order setDelivery(UUID orderId, UUID deliveryId) {
        Order order = findOrderById(orderId);
        order.setDeliveryId(deliveryId);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order deliverOrder(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.DELIVERED);
    }

    @Override
    @Transactional
    public Order failDeliverOrder(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.DELIVERY_FAILED);
    }

    @Override
    @Transactional
    public Order completeOrder(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.COMPLETED);
    }

    @Override
    @Transactional
    public Order setTotalPrice(UUID orderId, BigDecimal totalCost) {
        Order order = findOrderById(orderId);
        order.setTotalPrice(totalCost);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order setDeliveryPrice(UUID orderId, BigDecimal deliveryCost) {
        Order order = findOrderById(orderId);
        order.setDeliveryPrice(deliveryCost);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order assemblyOrder(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.ASSEMBLED);
    }

    @Override
    @Transactional
    public Order failAssemblyOrder(UUID orderId) {
        return updateOrderState(orderId, OrderStatus.ASSEMBLY_FAILED);
    }

    @Override
    public Order getOrderById(UUID orderId) {
        log.info("==> get order by id = {}", orderId);
        return findOrderById(orderId);
    }

    @Override
    @Transactional
    public Order savePaymentInfo(Order order) {
        Order oldOrder = findOrderById(order.getOrderId());
        oldOrder.setProductPrice(order.getProductPrice());
        oldOrder.setDeliveryPrice(order.getDeliveryPrice());
        oldOrder.setTotalPrice(order.getTotalPrice());
        oldOrder.setPaymentId(order.getPaymentId());
        oldOrder.setOrderStatus(OrderStatus.ON_PAYMENT);
        return orderRepository.save(order);
    }

    private Order updateOrderState(UUID orderId, OrderStatus newState) {
        Order order = findOrderById(orderId);
        order.setOrderStatus(newState);
        return orderRepository.save(order);
    }

    private Order findOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException("Заказ покупателя не найден")
        );
    }
}
