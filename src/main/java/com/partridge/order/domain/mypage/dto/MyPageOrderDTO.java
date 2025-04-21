package com.partridge.order.domain.mypage.dto;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class MyPageOrderDTO {
	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Response {
		private final List<MyPageResponseOrder> orders;
		private final Long totalCount;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class MyPageResponseOrder {
		private final String key;
		private final Long totalPrice;
		private final Long deliveryFee;
		private final String status;
	}
}
