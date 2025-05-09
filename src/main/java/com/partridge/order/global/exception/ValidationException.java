package com.partridge.order.global.exception;

public abstract class ValidationException extends RuntimeException {
	public ValidationException(String message) {
		super(message);
	}

	public abstract int getStatusCode();
}
