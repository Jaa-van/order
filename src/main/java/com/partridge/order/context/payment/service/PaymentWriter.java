package com.partridge.order.context.payment.service;

import org.springframework.stereotype.Component;

import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;
import com.partridge.order.context.payment.domain.model.Payment;
import com.partridge.order.context.payment.repository.PaymentRepository;
import com.partridge.order.context.payment.service.dto.PaymentServiceMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentWriter {
	private final PaymentRepository paymentRepository;

	public Payment postPayment(Payment payment) {
		return paymentRepository.save(payment);
	}
}
