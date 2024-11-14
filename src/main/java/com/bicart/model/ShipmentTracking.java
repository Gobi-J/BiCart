package com.bicart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bicart.constant.ShipmentStatus;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment_tracking")
public class ShipmentTracking extends BaseEntity{
    @Id
    private String id;
    private String location;
    private ShipmentStatus status;
}
