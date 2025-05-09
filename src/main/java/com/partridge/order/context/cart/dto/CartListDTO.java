package com.partridge.order.context.cart.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class CartListDTO {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"carts"})
	public static class Response {
		private List<ResponseCart> carts;
		private Integer totalCount;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"productId", "quantity", "inventory"})
	public static class ResponseCart {
		private Long productId;
		private Long quantity;
		private String key;
		private String name;
		private Long inventory;
		private Long price;
		private String description;

		@QueryProjection
		public ResponseCart(Long productId, Long quantity, String key, String name, Long inventory, Long price,
			String description) {
			this.productId = productId;
			this.quantity = quantity;
			this.key = key;
			this.name = name;
			this.inventory = inventory;
			this.price = price;
			this.description = description;
		}
	}
}
