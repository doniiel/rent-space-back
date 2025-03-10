package com.rentspace.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rentspace.paymentservice.entity.Payment;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
}

