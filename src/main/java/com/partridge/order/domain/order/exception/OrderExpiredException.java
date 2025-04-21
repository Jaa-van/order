package com.partridge.order.domain.order.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class OrderExpiredException extends BusinessException {
	private static final String message = EXPIRED_ORDER_EXCEPTION.getMessage();

	public OrderExpiredException() {
		super(message);
	}

	@Override
	public int getStatusCode() {
		return EXPIRED_ORDER_EXCEPTION.getCode();
	}
}
