package com.partridge.order.context.payment.infra.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class PaymentGatewayDTO {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"key", "price", "method"})
	public static class Request {
		private final String key;
		private final Long price;
		private final String method;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"status"})
	public static class Response {
		private final String status;
	}
}
