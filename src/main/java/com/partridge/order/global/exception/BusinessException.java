package com.partridge.order.global.exception;

public abstract class BusinessException extends RuntimeException {
	public BusinessException(String message) {
		super(message);
	}

	public abstract int getStatusCode();
}
