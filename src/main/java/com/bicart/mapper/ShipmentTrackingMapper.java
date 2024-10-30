package com.bicart.mapper;
import com.bicart.dto.ShipmentTrackingDto;
import com.bicart.model.ShipmentTracking;

public class ShipmentTrackingMapper {
    public static ShipmentTrackingDto modelToDto(ShipmentTracking shipmentTracking) {
        return ShipmentTrackingDto.builder()
                .id(shipmentTracking.getId())
                .location(shipmentTracking.getLocation())
                .status(shipmentTracking.getStatus())
                .build();
    }

    public static ShipmentTracking dtoToModel(ShipmentTrackingDto shipmentTrackingDto) {
        return ShipmentTracking.builder()
                .id(shipmentTrackingDto.getId())
                .location(shipmentTrackingDto.getLocation())
                .status(shipmentTrackingDto.getStatus())
                .build();
    }
}
