package com.partridge.order.domain.payment.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class PaymentGatewayFailException extends BusinessException {
	private static final String message = PAYMENT_GATEWAY_FAIL_EXCEPTION.getMessage();

	public PaymentGatewayFailException(String key) {
		super(key + message);
	}

	@Override
	public int getStatusCode() {
		return PAYMENT_GATEWAY_FAIL_EXCEPTION.getCode();
	}
}
