package com.partridge.order.context.order.constant;

import lombok.Getter;

@Getter
public enum OrderCommonCodeEnum {
	ORDER_WAITING("001"),
	ORDER_IN_PROGRESS("002"),
	ORDER_COMPLETE("003"),
	ORDER_EXPIRED("004");

	OrderCommonCodeEnum(String code) {
		this.code = code;
	}

	private final String code;
}
