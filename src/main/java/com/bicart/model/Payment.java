package com.bicart.model;

import com.bicart.constant.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="payment")
@Setter
@Getter
@Builder
public class Payment extends BaseEntity {

    @Id
    private String  id;

    @Column(name = "payment_mode", nullable = false)
    private String paymentMode;

    @Column(nullable=false)
    private long price;

    @Column(nullable=false)
    private PaymentStatus status;
}
