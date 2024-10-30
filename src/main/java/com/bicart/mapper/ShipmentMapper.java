package com.bicart.mapper;

import com.bicart.dto.ShipmentDto;
import com.bicart.model.Shipment;

public class ShipmentMapper {
    public static ShipmentDto modelToDto(Shipment shipment) {
        return ShipmentDto.builder()
                .id(shipment.getId())
                .currentStatus(shipment.getCurrentStatus())
                .build();
    }

    public static Shipment dtoToModel(ShipmentDto shipmentDto) {
        return Shipment.builder()
                .id(shipmentDto.getId())
                .currentStatus(shipmentDto.getCurrentStatus())
                .build();
    }
}
