package com.partridge.order.context.payment.service;

import static com.partridge.order.context.payment.constant.PaymentCommonCode.*;

import org.springframework.stereotype.Component;

import com.partridge.order.context.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentReader {
	private final PaymentRepository paymentRepository;

	public Boolean existsPaymentByOrderIdAndStatus(Long orderId, String status) {
		return paymentRepository.existsPaymentByOrderId(orderId, status);
	}
}
