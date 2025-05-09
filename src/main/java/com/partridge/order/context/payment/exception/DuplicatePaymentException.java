package com.partridge.order.context.payment.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class DuplicatePaymentException extends BusinessException {
	private static final String message = DUPLICATE_PAYMENT_EXCEPTION.getMessage();

	public DuplicatePaymentException() {
		super(message);
	}

	@Override
	public int getStatusCode() {
		return DUPLICATE_PAYMENT_EXCEPTION.getCode();
	}
}
