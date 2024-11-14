package com.bicart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import com.bicart.constant.ShipmentStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipmentTrackingDto {

    private String id;

    @NotBlank(message = "Location is mandatory")
    private String location;

    private ShipmentStatus status;

}
