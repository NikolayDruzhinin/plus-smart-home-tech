package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.delivery.enums.DeliveryStatus;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    UUID deliveryId;
    @Column(name = "order_id")
    UUID orderId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_address_id")
    Address senderAddress;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_address_id")
    Address recipientAddress;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    DeliveryStatus deliveryStatus;
    Double deliveryWeight;
    Double deliveryVolume;
    boolean fragile;
}
