package com.partridge.order.domain.order.exception;

import static com.partridge.order.global.exception.ExceptionEnum.*;

import com.partridge.order.global.exception.BusinessException;

public class InventoryNotEnoughException extends BusinessException {
	private static final String message = INVENTORY_NOT_ENOUGH.getMessage();

	public InventoryNotEnoughException(String value) {
		super(value + message);
	}

	@Override
	public int getStatusCode() {
		return INVENTORY_NOT_ENOUGH.getCode();
	}
}
