package com.bicart.dto;

import java.util.Set;

import com.bicart.constant.ShipmentStatus;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipmentDto {

    private String id;

    private ShipmentStatus currentStatus;

    private Set<ShipmentTrackingDto> shipmentTracking;
}
