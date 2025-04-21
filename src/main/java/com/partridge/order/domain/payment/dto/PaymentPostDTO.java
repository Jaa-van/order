package com.partridge.order.domain.payment.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class PaymentPostDTO {
	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		private final Long orderId;
		private final String key;
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
	@EqualsAndHashCode
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
	@EqualsAndHashCode
	public static class Response {
		private final String success;
	}
}
