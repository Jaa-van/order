package com.partridge.order.domain.product.dto;

import static com.partridge.order.global.util.KeyUtil.*;

import com.partridge.order.global.entity.Product;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ProductRegisterDTO {

	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		private final String name;
		private final Long quantity;
		private final Long price;
		private final String description;

		public Request(String name, Long quantity, Long price, String description) {
			this.name = name;
			this.quantity = quantity;
			this.price = price;
			this.description = description;
		}

		public Product toEntity() {
			return Product.builder()
				.key(generateKey())
				.name(name)
				.quantity(quantity)
				.price(price)
				.description(description)
				.build();
		}
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Response {
		private final String key;
	}
}
