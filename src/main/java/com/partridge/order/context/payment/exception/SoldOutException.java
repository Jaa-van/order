package com.partridge.order.context.payment.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class SoldOutException extends BusinessException {
	private static final String message = SOLD_OUT_EXCEPTION.getMessage();

	public SoldOutException(String key) {
		super(key + message);
	}

	@Override
	public int getStatusCode() {
		return SOLD_OUT_EXCEPTION.getCode();
	}
}
