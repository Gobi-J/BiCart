package com.bicart.model;


import java.util.Set;

import com.bicart.constant.ShipmentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="shipment")
@Getter
@Setter
@Builder
public class Shipment extends BaseEntity {
    @Id
    private String id;
    private ShipmentStatus currentStatus;

    @OneToMany(fetch = FetchType.EAGER, cascade = {
        CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<ShipmentTracking> shipmentTracking;
}
