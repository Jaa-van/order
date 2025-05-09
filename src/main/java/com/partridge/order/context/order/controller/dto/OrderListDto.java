package com.partridge.order.context.order.controller.dto;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class OrderListDto {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode(of = {"orders"})
	public static class Response {
		private final List<ResponseOrder> orders;
		private final Long totalCount;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class ResponseOrder {
		private final Long orderId;
		private final String key;
		private final Long totalPrice;
		private final Long deliveryFee;
		private final String status;
	}
}
