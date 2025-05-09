package com.partridge.order.context.product.controller.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ProductDetailDTO {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Response {
		private final Long productId;
		private final String key;
		private final String name;
		private final Long inventory;
		private final Long price;
		private final String description;
	}
}
