package com.partridge.order.global.exception;

import lombok.Getter;

@Getter
public enum ValidationExceptionEnum {
	INVALID_STATUS_EXCEPTION("상태가 유효하지 않습니다.", 500),
	DATABASE_POST_EXCEPTION("을(를) 생성에 실패했습니다.", 409);


	ValidationExceptionEnum(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	private final String message;
	private final Integer code;
}
