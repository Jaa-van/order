package com.partridge.order.context.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.partridge.order.context.payment.domain.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query(value = "select exists (select 1 from Payment p "
		+ "where p.orderId = :orderId and p.status = :status)")
	boolean existsPaymentByOrderId(Long orderId, String status);
}
