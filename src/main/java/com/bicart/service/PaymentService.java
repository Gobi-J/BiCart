package com.bicart.service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     * @param payment model.
     */
    protected void savePayment(Payment payment) {
        try {
            paymentRepository.save(payment);
            logger.info("Payment saved successfully with the Id : {} ", payment.getId());
        } catch (Exception e) {
            logger.error("Error in saving payment with the Id : {} ", payment.getId());
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays all payments.
     * </p>
     *
     * @return {@link Set <PaymentDto>} all the Payments.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<PaymentDto> getAllPayments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> paymentPage = paymentRepository.findAllByIsDeletedFalse(pageable);
        logger.info("Displayed payment details for page : {}", page);
        return paymentPage.getContent().stream()
                .map(PaymentMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Retrieves and displays the details of an payment.
     * </p>
     *
     * @param id the ID of the payment whose details are to be viewed
     * @return the Payment object.
     * @throws NoSuchElementException when occurred.
     */
    public PaymentDto getPaymentById(String id) {
        Payment payment = paymentRepository.findByIdAndIsDeletedFalse(id);
        if (payment == null) {
            throw new NoSuchElementException("Payment not found for the given id: " + id);
        }
        logger.info("Retrieved payment details for ID: {}", id);
        return PaymentMapper.modelToDto(payment);
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