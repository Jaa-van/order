package com.partridge.order.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
	NOT_FOUND_EXCEPTION("을(를) 찾을 수 없습니다.", 404),
	DATABASE_POST_EXCEPTION("을(를) 생성에 실패했습니다.", 409);

	ExceptionEnum(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	private final String message;
	private final Integer code;
}
