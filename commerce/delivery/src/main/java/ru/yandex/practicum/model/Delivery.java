package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.delivery.enums.DeliveryStatus;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "order_id")
    private UUID orderId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_address_id")
    private Address senderAddress;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_address_id")
    private Address recipientAddress;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private DeliveryStatus deliveryStatus;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;
}
