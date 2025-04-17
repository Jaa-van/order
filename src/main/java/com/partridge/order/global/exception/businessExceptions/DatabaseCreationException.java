package com.partridge.order.global.exception.businessExceptions;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class DatabaseCreationException extends BusinessException {
	private static final String message = DATABASE_POST_EXCEPTION.getMessage();

	public DatabaseCreationException(String message) {
		super(message);
	}

	@Override
	public int getStatusCode() {
		return 409;
	}
}
