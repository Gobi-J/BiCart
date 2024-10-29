package com.bicart.model;


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
@Table(name="user")
@Getter
@Builder
public class Shipment {

    @Id
    private int id;

    private ShipmentStatus currentStatus;
}
