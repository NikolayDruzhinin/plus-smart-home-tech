package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private final UUID paymentId;

    @Column(name = "order_id")
    private final UUID orderId;

    @Enumerated(value = EnumType.STRING)
    private PaymentState state;

    @Column(name = "total_cost")
    private final Double totalPayment;

    @Column(name = "delivery_cost")
    private final Double deliveryTotal;

    @Column(name = "fee_cost")
    private final Double feeTotal;
}
