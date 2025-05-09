package com.partridge.order.context.order.controller.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class OrderPostKeyDto {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"key"})
	public static class Resposne {
		private final String key;
	}
}
