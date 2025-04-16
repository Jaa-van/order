package com.partridge.order.domain.product.search.dto;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ProductSearchDTO {

	@Getter
	@ToString
	@EqualsAndHashCode
	public static class ListRequest {
		private final String keyword;
		private final String searchType;
		private final String orderBy;

		public ListRequest(String keyword, String searchType, String orderBy) {
			this.keyword = keyword;
			this.searchType = searchType;
			this.orderBy = orderBy;
		}
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class ListResponse {
		private final List<ProductResponse> products;
		private final int totalCount;
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class ProductResponse {

	}
}
