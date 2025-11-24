package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentState;
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.shoppingStore.client.ShoppingStoreClient;
import ru.yandex.practicum.shoppingStore.dto.ProductDto;
import ru.yandex.practicum.utils.PaymentUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto order) {
        Payment savedPayment = paymentRepository.save(getNewPayment(order));
        return paymentMapper.map(savedPayment);
    }

    @Override
    public BigDecimal getTotalCost(OrderDto order) {
        return calcTotalCost(order);
    }

    @Override
    @Transactional
    public void paymentSuccess(UUID orderId) {
        updatePaymentState(orderId, PaymentState.SUCCESS);
        orderClient.successPayOrder(orderId);
    }

    @Override
    public BigDecimal getProductCost(OrderDto order) {
        return calcProductsCost(order);
    }

    @Override
    @Transactional
    public void paymentFailed(UUID orderId) {
        updatePaymentState(orderId, PaymentState.FAILED);
        orderClient.failPayOrder(orderId);
    }

    private void updatePaymentState(UUID orderId, PaymentState newState) {
        Payment payment = getPaymentByOrderId(orderId);
        payment.setState(newState);
        paymentRepository.save(payment);
    }

    private Payment getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoPaymentFoundException("Указанная оплата не найдена")
        );
    }

    private Payment getNewPayment(OrderDto order) {
        BigDecimal fee = calcFeeCost(order.getProductPrice());
        return Payment.builder()
                .orderId(order.getOrderId())
                .state(PaymentState.PENDING)
                .totalPayment(order.getTotalPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .feeTotal(fee)
                .build();
    }

    private BigDecimal calcTotalCost(OrderDto order) {
        BigDecimal productCost = calcProductsCost(order);
        BigDecimal deliveryCost = order.getDeliveryPrice();

        return productCost.add(calcFeeCost(productCost)).add(deliveryCost);
    }

    private BigDecimal calcProductsCost(OrderDto order) {
        if (order == null || order.getProducts() == null) {
            throw new IllegalArgumentException("Order and products cannot be null");
        }

        if (order.getProducts().isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Получаем продукты из хранилища
        Map<UUID, ProductDto> products = shoppingStoreClient.getProductByIds(order.getProducts().keySet())
                .stream()
                .collect(Collectors.toMap(ProductDto::getProductId, Function.identity()));

        BigDecimal totalCost = BigDecimal.ZERO;

        for (Map.Entry<UUID, Integer> orderProduct : order.getProducts().entrySet()) {
            UUID productId = orderProduct.getKey();
            int quantity = orderProduct.getValue();

            if (!products.containsKey(productId)) {
                throw new NotEnoughInfoInOrderToCalculateException(
                        "Недостаточно информации в заказе для расчёта: продукт " + productId + " не найден"
                );
            }

            ProductDto product = products.get(productId);
            if (product.getPrice() == null) {
                throw new NotEnoughInfoInOrderToCalculateException(
                        "Цена продукта " + productId + " не указана"
                );
            }

            BigDecimal productCost = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalCost = totalCost.add(productCost);
        }

        return totalCost;
    }

    private BigDecimal calcFeeCost(BigDecimal cost) {
        return cost.multiply(BigDecimal.valueOf(PaymentUtil.BASE_VAT_RATE));
    }
}
