package com.partridge.order.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
	NOT_FOUND_EXCEPTION("을(를) 찾을 수 없습니다.", 404),
	DATABASE_POST_EXCEPTION("을(를) 생성에 실패했습니다.", 409),
	INVENTORY_NOT_ENOUGH("의 재고가 부족합니다.", 409),
	DUPLICATE_ORDER_REQUEST_EXCEPTION("중복된 주문 요청입니다.", 409),
	EXPIRED_ORDER_EXCEPTION("만료된 주문입니다.", 400),
	PAYMENT_GATEWAY_FAIL_EXCEPTION("의 결제가 실패했습니다.", 400),
	SOLD_OUT_EXCEPTION("의 재고가 부족합니다.", 409);

	ExceptionEnum(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	private final String message;
	private final Integer code;
}
