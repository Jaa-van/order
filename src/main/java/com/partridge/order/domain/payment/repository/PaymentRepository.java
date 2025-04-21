package com.partridge.order.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.partridge.order.global.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
