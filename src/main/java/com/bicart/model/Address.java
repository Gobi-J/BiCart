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
@Table(name="address")
@Getter
@Builder
public class Address extends BaseEntity {

    @Id
    private int id;

    @Column(nullable=false)
    private String doorNumber;

    @Column(nullable=false)
    private String street;

    @Column(nullable=false)
    private String city;

    @Column(nullable=false)
    private String state;

    @Column(nullable=false)
    private String country;

    @Column(nullable=false)
    private String pincode;

    @Column(nullable=false)
    private String phone;
}
