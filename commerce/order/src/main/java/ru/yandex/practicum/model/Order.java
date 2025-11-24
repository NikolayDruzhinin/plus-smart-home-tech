package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    UUID orderId;
    @Column(name = "user_name")
    String userName;
    @Column(name = "cart_id")
    UUID cartId;
    @ElementCollection
    @CollectionTable(name="order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products;
    @Column(name = "payment_id")
    UUID paymentId;
    @Column(name = "delivery_id")
    UUID deliveryId;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    OrderStatus orderStatus;
    @Column(name = "delivery_weight")
    Double deliveryWeight;
    @Column(name = "delivery_volume")
    Double deliveryVolume;
    boolean fragile;
    @Column(name = "total_price")
    BigDecimal totalPrice;
    @Column(name = "delivery_price")
    BigDecimal deliveryPrice;
    @Column(name = "product_price")
    BigDecimal productPrice;
}
