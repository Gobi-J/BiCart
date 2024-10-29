package com.bicart.model;


import com.bicart.constant.ShipmentStatus;
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
@Table(name="shipment")
@Getter
@Builder
public class Shipment extends BaseEntity {
    @Id
    private String id;
    private ShipmentStatus currentStatus;
}
