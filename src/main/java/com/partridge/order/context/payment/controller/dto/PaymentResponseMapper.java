package com.partridge.order.context.payment.controller.dto;

import org.springframework.stereotype.Component;

import com.partridge.order.context.payment.domain.model.Payment;

@Component
public class PaymentResponseMapper {
	public PaymentPostDTO.Response toPaymentPostResponse(Payment payment) {
		return PaymentPostDTO.Response.builder()
			.paymentId(payment.getId())
			.orderId(payment.getOrderId())
			.method(payment.getMethod())
			.status(payment.getStatus())
			.build();
	}
}
