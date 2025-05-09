package com.partridge.order.context.product.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private Long productId;
	private String key;
	private String name;
	private Long inventory;
	private Long price;
	private String description;
}
