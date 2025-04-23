package com.partridge.order.global.lock.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class AcquireLockTimeOutException extends BusinessException {
	private static final String message = ACQUIRE_LOCK_TIME_OUT_EXCEPTION.getMessage();

	public AcquireLockTimeOutException(String name) {
		super(name + message);
	}

	@Override
	public int getStatusCode() {
		return ACQUIRE_LOCK_TIME_OUT_EXCEPTION.getCode();
	}
}
