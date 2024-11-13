package com.bicart.dto;

import java.util.Set;

import com.bicart.constant.ShipmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipmentDto {
    @JsonIgnore
    private String id;

    private ShipmentStatus currentStatus;
    @JsonIgnore
    private Set<ShipmentTrackingDto> shipmentTracking;
}
