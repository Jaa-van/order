package com.partridge.order.context.cart.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class CartPostDTO {
	@Getter
	@ToString
	@EqualsAndHashCode(of = {"userId", "carts"})
	public static class Request {
		@NotNull
		private final Long userId;
		@Valid
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
	@EqualsAndHashCode(of = {"productId", "quantity"})
	public static class RequestCart {
		@NotNull
		@Min(1)
		private final Long productId;
		@NotNull
		private final Long quantity;

		@JsonCreator
		public RequestCart(@JsonProperty("productId") Long productId,
			@JsonProperty("quantity") Long quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}
	}
}
