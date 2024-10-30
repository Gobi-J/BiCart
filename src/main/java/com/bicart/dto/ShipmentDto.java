package com.bicart.dto;

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
}
