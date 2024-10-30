package com.bicart.dto;

import com.bicart.constant.ShipmentStatus;
import com.bicart.model.Shipment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

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
    private ShipmentDto shipment;

}
