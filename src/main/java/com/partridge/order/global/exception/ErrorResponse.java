package com.partridge.order.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
	private final String message;
	private final int status;

	@Builder
	public ErrorResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}
}
