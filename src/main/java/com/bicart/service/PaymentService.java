package com.bicart.service;

import com.bicart.dto.PaymentDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.PaymentMapper;
import com.bicart.model.Payment;
import com.bicart.repository.PaymentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

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
            logger.info("Payment saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving payment");
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
    public PaymentDto addPayment(PaymentDto paymentDTO) throws CustomException {
        try {
            Payment payment = PaymentMapper.dtoToModel((paymentDTO));
            paymentRepository.save(payment);
            PaymentDto paymentDto = PaymentMapper.modelToDto((payment));
            logger.info("Payment added successfully with ID: {}", paymentDto.getId());
            return paymentDto;
        } catch (Exception e) {
            logger.error("Error adding a payment", e);
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
    public Set<PaymentDto> getAllPayments(int page, int size) throws CustomException {
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
    public PaymentDto getPaymentById(String id) throws NoSuchElementException, CustomException {
        try {
            Payment payment = paymentRepository.findByIdAndIsDeletedFalse(id);
            if (payment == null) {
                throw new NoSuchElementException("Payment not found for the given id: " + id);
            }
            logger.info("Retrieved payment details for ID: {}", id);
            return PaymentMapper.modelToDto(payment);
        } catch (NoSuchElementException e) {
            logger.error("Payment not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error in retrieving an payment : {}", id, e);
            throw new CustomException("Server error!!", e);
        }
    }
}







