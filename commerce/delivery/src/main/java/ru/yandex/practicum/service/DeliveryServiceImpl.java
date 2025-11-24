package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.delivery.dto.DeliveryRequest;
import ru.yandex.practicum.delivery.enums.DeliveryStatus;
import ru.yandex.practicum.exception.DeliveryNotFoundException;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.utils.DeliveryUtil;
import ru.yandex.practicum.warehouse.client.WarehouseClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private static final double WEIGHT_RATE = 0.3;
    private static final double VOLUME_RATE = 0.2;

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final AddressMapper addressMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        log.info("Creating delivery for order id = {}", deliveryDto.getOrderId());
        Delivery delivery = buildDeliveryEntity(deliveryDto);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    @Override
    @Transactional
    public void completeDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.deliverOrder(orderId);
    }

    @Override
    @Transactional
    public void confirmPickup(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);
        deliveryRepository.save(delivery);

        DeliveryRequest request = getNewShippedToDeliveryRequest(delivery);
        warehouseClient.shippedToDelivery(request);
    }

    @Override
    @Transactional
    public void failDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.FAILED);
        deliveryRepository.save(delivery);
        orderClient.failDeliverOrder(orderId);
    }

    @Override
    public BigDecimal calculateDeliveryCost(OrderDto orderDto) {
        if (orderDto == null) {
            throw new IllegalArgumentException("OrderDto cannot be null");
        }

        Delivery delivery = getDeliveryByOrderId(orderDto.getOrderId());
        if (delivery == null) {
            throw new IllegalArgumentException("Delivery not found for orderId: " + orderDto.getOrderId());
        }

        log.info("delivery for calc cost: {}", delivery);

        // Инициализируем базовую стоимость доставки
        BigDecimal cost = BigDecimal.valueOf(DeliveryUtil.BASE_DELIVERY_PRICE);

        // Добавляем коэффициент от адреса отправителя
        BigDecimal fromAddressCoef = getCoefByFromAddress(delivery.getSenderAddress());
        cost = cost.add(BigDecimal.valueOf(DeliveryUtil.BASE_DELIVERY_PRICE).multiply(fromAddressCoef));

        // Применяем коэффициент хрупкости
        BigDecimal fragileCoef = getFragileCoefficient(orderDto.isFragile());
        cost = cost.multiply(fragileCoef);

        // Добавляем стоимость за вес
        BigDecimal weightCost = BigDecimal.valueOf(orderDto.getDeliveryWeight())
                .multiply(BigDecimal.valueOf(WEIGHT_RATE));
        cost = cost.add(weightCost);

        // Добавляем стоимость за объем
        BigDecimal volumeCost = BigDecimal.valueOf(orderDto.getDeliveryVolume())
                .multiply(BigDecimal.valueOf(VOLUME_RATE));
        cost = cost.add(volumeCost);

        // Применяем коэффициент расстояния между адресами
        BigDecimal distanceCoef = getCoefByToAddress(delivery.getSenderAddress(), delivery.getRecipientAddress());
        cost = cost.multiply(distanceCoef);

        // Округляем до 2 знаков (копеек)
        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    BigDecimal getCoefByFromAddress(Address address) {
        String addressStr = address.toString();
        if (addressStr.contains("ADDRESS_1")) {
            return BigDecimal.valueOf(DeliveryUtil.ADDRESS_1_ADDRESS_COEF);
        } else if (addressStr.contains("ADDRESS_2")) {
            return BigDecimal.valueOf(DeliveryUtil.ADDRESS_2_ADDRESS_COEF);
        } else {
            return BigDecimal.valueOf(DeliveryUtil.BASE_ADDRESS_COEF);
        }
    }

    BigDecimal getCoefByToAddress(Address from, Address to) {
        if (!from.getStreet().equals(to.getStreet())) {
            return BigDecimal.valueOf(DeliveryUtil.DIFF_STREET_ADDRESS_COEF);
        }

        return BigDecimal.valueOf(1.0);
    }

    BigDecimal getFragileCoefficient(boolean isFragile) {
        double val = isFragile ? DeliveryUtil.FRAGILE_COEF : 1.0;
        return BigDecimal.valueOf(val);
    }

    private Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(
                () -> new DeliveryNotFoundException("Не найдена доставка")
        );
    }

    private DeliveryRequest getNewShippedToDeliveryRequest(Delivery delivery) {
        return new DeliveryRequest(
                delivery.getOrderId(),
                delivery.getDeliveryId()
        );
    }

    private Delivery buildDeliveryEntity(DeliveryDto dto) {
        return Delivery.builder()
                .orderId(dto.getOrderId())
                .senderAddress(addressMapper.toEntity(dto.getSenderAddress()))
                .recipientAddress(addressMapper.toEntity(dto.getRecipientAddress()))
                .deliveryStatus(DeliveryStatus.CREATED)
                .build();
    }
}
