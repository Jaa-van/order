package com.partridge.order.global.exception.businessExceptions;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class NotFoundException extends BusinessException {
	private static final String message = NOT_FOUND_EXCEPTION.getMessage();

	public NotFoundException(String value) {
		super(value + message);
	}

	@Override
	public int getStatusCode() {
		return NOT_FOUND_EXCEPTION.getCode();
	}
}
