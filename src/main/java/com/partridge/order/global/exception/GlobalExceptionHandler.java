package com.partridge.order.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(BusinessException exception) {
		int statusCode = exception.getStatusCode();

		return ResponseEntity.status(statusCode)
			.body(ErrorResponse.builder()
				.status(statusCode)
				.message(exception.getMessage())
				.build());
	}

	@ExceptionHandler(Exception.class)
	public ErrorResponse handleAllException(Exception e) {
		return ErrorResponse.builder()
			.status(500)
			.message("서버에서 오류가 발생했습니다.")
			.build();
	}
}
