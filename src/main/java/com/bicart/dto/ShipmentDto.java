package com.bicart.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bicart.constant.ShipmentStatus;

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
