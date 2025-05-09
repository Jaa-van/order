package com.partridge.order.context.product.controller.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ProductListDTO {

	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		private final String keyword;
		private final String searchType;
		private final String orderBy;
		private final String orderDirection;

		public Request(String keyword, String searchType, String orderBy, String orderDirection) {
			this.keyword = keyword;
			this.searchType = searchType;
			this.orderBy = orderBy;
			this.orderDirection = orderDirection;
		}
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Response {
		private final List<ResponseProduct> products;
		private final int totalCount;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class ResponseProduct {
		private final Long productId;
		private final String key;
		private final String name;
		private final Long inventory;
		private final Long price;
		private final String description;

		public ResponseProduct(Long productId, String key, String name, Long inventory, Long price, String description) {
			this.productId = productId;
			this.key = key;
			this.name = name;
			this.inventory = inventory;
			this.price = price;
			this.description = description;
		}
	}
}
