package com.partridge.order.context.order.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.ValidationException;

public class InvalidStatusException extends ValidationException {
	private static final String message = INVENTORY_NOT_ENOUGH.getMessage();

	public InvalidStatusException(String value) {
		super(value + message);
	}

	@Override
	public int getStatusCode() {
		return INVENTORY_NOT_ENOUGH.getCode();
	}
}
