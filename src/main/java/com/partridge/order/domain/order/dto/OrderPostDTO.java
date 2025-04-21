package com.partridge.order.domain.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class OrderPostDTO {
	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		private final Long userId;
		private final String key;
		private final List<RequestProduct> products;

		@JsonCreator
		public Request(@JsonProperty("userId") Long userId,
			@JsonProperty("key") String key,
			@JsonProperty("products") List<RequestProduct> products) {
			this.userId = userId;
			this.key = key;
			this.products = products;
		}
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	public static class RequestProduct {
		private final Long productId;
		private final Long quantity;

		@JsonCreator
		public RequestProduct(@JsonProperty("productId") Long productId,
			@JsonProperty("quantity") Long quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Response {
		private final Long orderId;
		private final String key;
		private final Long totalPrice;
		private final Long deliveryFee;
		private final String status;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class ProductInformation {
		private Long productId;
		private Long price;
		private Long inventory;
		private String name;
	}
}
