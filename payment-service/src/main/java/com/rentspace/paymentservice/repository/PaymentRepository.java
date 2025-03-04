package com.rentspace.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rentspace.paymentservice.entity.Payment;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
}

