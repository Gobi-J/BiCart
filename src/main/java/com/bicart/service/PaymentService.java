package com.bicart.service;

import com.bicart.constant.OrderStatus;
import com.bicart.constant.PaymentStatus;
import com.bicart.dto.PaymentDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.PaymentMapper;
import com.bicart.model.Order;
import com.bicart.model.Payment;
import com.bicart.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void savePayment(Payment payment) {
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
     * Creates a new Payment object and saves it in the repository.
     * </p>
     *
     * @param paymentDTO to create new payment.
     * @return the created paymentDto object.
     * @throws CustomException, DuplicateKeyException if exception is thrown.
     */
    public PaymentDto addPayment(PaymentDto paymentDTO, String orderId) {
        try {
            Payment payment = PaymentMapper.dtoToModel((paymentDTO));
            Order order = orderService.getOrderById(orderId);
            payment.setOrder(order);
            paymentRepository.save(payment);
            PaymentDto paymentDto = PaymentMapper.modelToDto((payment));
            logger.info("Payment added successfully with ID: {}", paymentDto.getId());
            return paymentDto;
        } catch (Exception e) {
            logger.error("Error adding a payment with order ID: {}", orderId);
            throw new CustomException("Server Error!!!!", e);
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
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Payment> paymentPage = paymentRepository.findAllByIsDeletedFalse(pageable);
            logger.info("Displayed payment details for page : {}", page);
            return paymentPage.getContent().stream()
                    .map(PaymentMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving all payments", e);
            throw new CustomException("Server Error!!!!", e);
        }
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
        try {
            Payment payment = paymentRepository.findByIdAndIsDeletedFalse(id);
            if (payment == null) {
                throw new NoSuchElementException("Payment not found for the given id: " + id);
            }
            logger.info("Retrieved payment details for ID: {}", id);
            return PaymentMapper.modelToDto(payment);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Payment not found for the Id: {}", id, e);
                throw e;
            }
            logger.error("Error in retrieving a payment with the id : {}", id, e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Updates the payment details.
     * </p>
     *
     * @param orderId the ID of the order for which payment is to be updated.
     * @param paymentDto the updated payment details.
     * @return {@link PaymentDto} updated Payment object.
     * @throws CustomException if any custom exception is thrown.
     */
    public PaymentDto createPayment(String orderId, PaymentDto paymentDto) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                throw new NoSuchElementException("Order not found with id: " + orderId);
            }
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new DuplicateKeyException("Order is not in pending state");
            }
            Payment payment = PaymentMapper.dtoToModel(paymentDto);
            payment.setCreatedAt(new Date());
            payment.setStatus(PaymentStatus.PAID);
            order.setStatus(OrderStatus.PAID);
            order.setPayment(payment);
            orderService.saveOrder(order);
            return PaymentMapper.modelToDto(payment);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Order not found", e);
                throw e;
            }
            if (e instanceof DuplicateKeyException) {
                logger.error("Order is not in pending state", e);
                throw e;
            }
            logger.error("Error in creating payment", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }
}







