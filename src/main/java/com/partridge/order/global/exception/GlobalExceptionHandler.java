package com.partridge.order.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(BusinessException exception) {
		int statusCode = exception.getStatusCode();

		return ResponseEntity.status(statusCode)
			.body(ErrorResponse.builder()
				.status(statusCode)
				.message(exception.getMessage())
				.build());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleAllException(Exception e) {
		logger.error("서버에서 오류가 발생했습니다. 예외 메시지: {}", e.getMessage(), e);

		return ErrorResponse.builder()
			.status(500)
			.message("서버에서 오류가 발생했습니다.")
			.build();
	}
}
