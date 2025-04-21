package com.partridge.order.domain.cart.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class CartPostDTO {
	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		private final Long userId;
		private final List<RequestCart> carts;

		@JsonCreator
		public Request(@JsonProperty("userId") Long userId,
			@JsonProperty("carts") List<RequestCart> carts) {
			this.userId = userId;
			this.carts = carts;
		}
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	public static class RequestCart {
		private final Long productId;
		private final Long quantity;

		@JsonCreator
		public RequestCart(@JsonProperty("productId") Long productId,
			@JsonProperty("quantity") Long quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}
	}
}
