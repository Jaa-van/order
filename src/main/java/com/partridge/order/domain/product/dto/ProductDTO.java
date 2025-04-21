package com.partridge.order.domain.product.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ProductDTO {
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
