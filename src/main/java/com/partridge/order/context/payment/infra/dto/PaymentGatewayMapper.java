package com.partridge.order.context.payment.infra.dto;

import org.springframework.stereotype.Component;

import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;

@Component
public class PaymentGatewayMapper {
	public PaymentGatewayDTO.Request toRequest(PaymentPostDTO.Request request, Long totalPrice) {
		return PaymentGatewayDTO.Request.builder()
			.method(request.getMethod())
			.key(request.getKey())
			.price(totalPrice)
			.build();
	}
}
