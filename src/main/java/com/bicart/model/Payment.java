package com.bicart.model;

import com.bicart.constant.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="payment")
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
