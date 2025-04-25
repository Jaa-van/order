package com.partridge.order.domain.order.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class OrderPostKeyDTO {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"key"})
	public static class Resposne {
		private final String key;
	}
}
