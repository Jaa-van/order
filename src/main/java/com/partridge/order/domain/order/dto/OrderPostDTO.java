package com.partridge.order.domain.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class OrderPostDTO {
	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		@NotNull
		private final Long userId;
		@NotBlank
		@Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "key 형식이 잘못되었습니다.")
		private final String key;
		@Valid
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
		@NotNull
		private final Long productId;
		@NotNull
		@Min(1)
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
