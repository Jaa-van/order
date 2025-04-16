package com.partridge.order.global.exception.exception;

import static com.partridge.order.global.constant.ConstantValue.*;

import com.partridge.order.global.exception.BusinessException;

public class NotFoundException extends BusinessException {
	private static final String message = CUSTOM_NOT_FOUND_EXCEPTION;

	public NotFoundException(String name) {
		super(name + message);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
