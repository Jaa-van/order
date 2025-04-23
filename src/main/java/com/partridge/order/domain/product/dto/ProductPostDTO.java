package com.partridge.order.domain.product.dto;

import static com.partridge.order.global.util.KeyUtil.*;

import com.partridge.order.global.entity.Product;
import com.querydsl.core.annotations.QueryProjection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ProductPostDTO {

	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		@NotBlank
		private final String name;
		@NotNull
		private final Long inventory;
		@NotNull
		private final Long price;
		private final String description;

		public Request(String name, Long inventory, Long price, String description) {
			this.name = name;
			this.inventory = inventory;
			this.price = price;
			this.description = description;
		}
	}

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

		@QueryProjection
		public Response(Long productId, String key, String name, Long inventory, Long price, String description) {
			this.productId = productId;
			this.key = key;
			this.name = name;
			this.inventory = inventory;
			this.price = price;
			this.description = description;
		}
	}
}
