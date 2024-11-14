package com.bicart.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.bicart.constant.OrderStatus;
import com.bicart.constant.PaymentStatus;
import com.bicart.dto.PaymentDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.PaymentMapper;
import com.bicart.model.Order;
import com.bicart.model.Payment;
import com.bicart.repository.PaymentRepository;

/**
 * <p>
 * Service class that handles business logic related to payments.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    private static final Logger logger = LogManager.getLogger(PaymentService.class);

    /**
     * <p>
     * Saves a payment.
     * </p>
     *
     * @param payment details to be saved
     * @throws CustomException if any issue occurs while saving the payment.
     */
    protected Payment savePayment(Payment payment) {
        try {
            return paymentRepository.save(payment);
        } catch (Exception e) {
            logger.error("Error in saving payment with the Id : {} ", payment.getId());
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Updates the payment details.
     * </p>
     *
     * @param orderId    the ID of the order for which payment is to be updated.
     * @param paymentDto the updated payment details.
     * @throws CustomException if any custom exception is thrown.
     */
    public void createPayment(String userId, String orderId, PaymentDto paymentDto) {
        Order order = orderService.getOrderModelById(userId, orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found with id: " + orderId);
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new DuplicateKeyException("Order is not in pending state");
        }
        Payment payment = PaymentMapper.dtoToModel(paymentDto);
        payment.setId(UUID.randomUUID().toString());
        payment.setAudit(userId);
        payment.setStatus(PaymentStatus.PAID);
        orderService.notifyPayment(userId, orderId, payment);
    }
}