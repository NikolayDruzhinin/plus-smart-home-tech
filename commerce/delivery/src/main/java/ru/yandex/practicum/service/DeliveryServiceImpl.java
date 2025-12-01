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
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.constants.DeliveryConstants;
import ru.yandex.practicum.warehouse.client.WarehouseClient;

import java.util.UUID;

import static ru.yandex.practicum.constants.DeliveryConstants.*;

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
        ru.yandex.practicum.model.Delivery delivery = buildDeliveryEntity(deliveryDto);
        ru.yandex.practicum.model.Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    @Override
    @Transactional
    public void completeDelivery(UUID orderId) {
        ru.yandex.practicum.model.Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.deliverOrder(orderId);
    }

    @Override
    @Transactional
    public void confirmPickup(UUID orderId) {
        ru.yandex.practicum.model.Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);
        deliveryRepository.save(delivery);

        DeliveryRequest request = getNewShippedToDeliveryRequest(delivery);
        warehouseClient.shippedToDelivery(request);
    }

    @Override
    @Transactional
    public void failDelivery(UUID orderId) {
        ru.yandex.practicum.model.Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.FAILED);
        deliveryRepository.save(delivery);
        orderClient.failDeliverOrder(orderId);
    }

    @Override
    public Double calculateDeliveryCost(OrderDto orderDto) {
        if (orderDto == null) {
            throw new IllegalArgumentException("OrderDto cannot be null");
        }

        ru.yandex.practicum.model.Delivery delivery = getDeliveryByOrderId(orderDto.getOrderId());
        log.info("delivery for calc cost: {}", delivery);

        // Инициализируем базовую стоимость доставки
        double cost = BASE_DELIVERY_PRICE;

        // Добавляем коэффициент от адреса отправителя
        var fromAddressCoef = getCoefByFromAddress(delivery.getSenderAddress());
        cost += BASE_DELIVERY_PRICE * fromAddressCoef;

        // Применяем коэффициент хрупкости
        var fragileCoeff = orderDto.isFragile() ? DeliveryConstants.FRAGILE_COEF : 1.0;
        cost *= fragileCoeff;


        // Добавляем стоимость за вес
        var weightCost = orderDto.getDeliveryWeight() * WEIGHT_RATE;
        cost += weightCost;

        // Добавляем стоимость за объем
        var volumeCost = orderDto.getDeliveryVolume() * VOLUME_RATE;
        cost += volumeCost;

        // Применяем коэффициент расстояния между адресами
        var distanceCoef = !delivery.getSenderAddress().getStreet()
                .equals(delivery.getRecipientAddress().getStreet()) ? DIFF_STREET_ADDRESS_COEF : 1.0;
        cost *= distanceCoef;

        // Округляем до 2 знаков (копеек)
        return Math.round(cost * 100.0) / 100.0;
    }

    Double getCoefByFromAddress(Address address) {
        String addressStr = address.toString();
        if (addressStr.contains("ADDRESS_1")) {
            return ADDRESS_1_ADDRESS_COEF;
        } else if (addressStr.contains("ADDRESS_2")) {
            return ADDRESS_2_ADDRESS_COEF;
        } else {
            return BASE_ADDRESS_COEF;
        }
    }

    private ru.yandex.practicum.model.Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(
                () -> new DeliveryNotFoundException("Delivery not found")
        );
    }

    private DeliveryRequest getNewShippedToDeliveryRequest(ru.yandex.practicum.model.Delivery delivery) {
        return new DeliveryRequest(
                delivery.getOrderId(),
                delivery.getDeliveryId()
        );
    }

    private ru.yandex.practicum.model.Delivery buildDeliveryEntity(DeliveryDto dto) {
        return ru.yandex.practicum.model.Delivery.builder()
                .orderId(dto.getOrderId())
                .senderAddress(addressMapper.toEntity(dto.getSenderAddress()))
                .recipientAddress(addressMapper.toEntity(dto.getRecipientAddress()))
                .deliveryStatus(DeliveryStatus.CREATED)
                .build();
    }
}
