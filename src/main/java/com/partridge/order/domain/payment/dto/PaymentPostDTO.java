package com.partridge.order.domain.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class PaymentPostDTO {
	@Getter
	@Builder
	@ToString
	public static class Request {
		@NotNull
		private final Long orderId;
		@NotBlank
		@Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "key 형식이 잘못되었습니다.")
		private final String key;
		@NotNull
		private final String method;

		public Request(Long orderId, String key, String method) {
			this.orderId = orderId;
			this.key = key;
			this.method = method;
		}
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"orderId", "productId", "quantity", "inventory"})
	public static class OrderProductInventory {
		private final Long orderId;
		private final Long productId;
		private final Long quantity;
		private final String name;
		private final Long inventory;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"paymentId", "status"})
	public static class Response {
		private final Long paymentId;
		private final Long orderId;
		private final String method;
		private final String status;
	}
}
