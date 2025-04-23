package com.partridge.order.domain.payment.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class PaymentAlreadyCompleteException extends BusinessException {
	private static final String message = PAYMENY_ALREADY_COMPLETE_EXCEPTION.getMessage();

	public PaymentAlreadyCompleteException() {
		super(message);
	}

	@Override
	public int getStatusCode() {
		return PAYMENY_ALREADY_COMPLETE_EXCEPTION.getCode();
	}
}
