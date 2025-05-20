package com.partridge.order.context.payment.service.dto;

import static com.partridge.order.context.payment.constant.PaymentCommonCode.*;

import org.springframework.stereotype.Component;

import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;
import com.partridge.order.context.payment.domain.model.Payment;

@Component
public class PaymentServiceMapper {
	public Payment toEntityWithPaymentComplete(PaymentPostDTO.Request request) {
		return Payment.builder()
			.orderId(request.getOrderId())
			.method(request.getMethod())
			.status(PAYMENT_COMPLETE)
			.build();
	}

	public Payment toEntityWithPaymentFailed(PaymentPostDTO.Request request) {
		return Payment.builder()
			.orderId(request.getOrderId())
			.method(request.getMethod())
			.status(PAYMENT_FAILED)
			.build();
	}
}
