package com.bicart.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bicart.constant.ShipmentStatus;
import com.bicart.dto.ShipmentDto;
import com.bicart.model.Order;
import com.bicart.model.Shipment;
import com.bicart.repository.ShipmentRepository;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    private Shipment shipment;
    private ShipmentDto shipmentDto;
    private Order order;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentTrackingService shipmentTrackingService;

    @InjectMocks
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {

        shipmentDto = ShipmentDto.builder()
                .currentStatus(ShipmentStatus.PENDING)
                .build();

        shipment = Shipment.builder()
                .currentStatus(ShipmentStatus.PENDING)
                .build();

        order = Order.builder()
                .shipment(shipment)
                .build();
    }

    @Test
    void testAddShipmentSuccess() {
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);
        ShipmentDto result = shipmentService.addShipment(shipmentDto);
        assertNotNull(result);
        assertEquals(shipmentDto.getCurrentStatus(), result.getCurrentStatus());
    }

    @Test
    void testInitializeShipmentSuccess() {
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);;
        doCallRealMethod().when(shipmentTrackingService).initializeShipping();
        shipmentService.initializeShipment(order);
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void cancelShipment() {
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);
        shipmentService.cancelShipment(order);
        verify(shipmentRepository).save(any(Shipment.class));
    }
}