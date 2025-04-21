package com.partridge.order.domain.order.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class DuplicateOrderRequestException extends BusinessException {
	private static final String message = DUPLICATE_ORDER_REQUEST_EXCEPTION.getMessage();

	public DuplicateOrderRequestException() {
		super(message);
	}

	@Override
	public int getStatusCode() {
		return DUPLICATE_ORDER_REQUEST_EXCEPTION.getCode();
	}
}
