package com.bicart.model;

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
public class Payment {

    @Id
    private int id;

    @Column(name = "payment_mode", nullable = false)
    private String paymentMode;

    @Column(nullable=false)
    private String price;

    @Column(nullable=false)
    private String status;
}
