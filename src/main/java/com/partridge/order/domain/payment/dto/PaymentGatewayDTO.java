package com.partridge.order.domain.payment.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class PaymentGatewayDTO {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Request {
		private final String key;
		private final Long price;
		private final String method;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Response {
		private final String status;
	}
}
