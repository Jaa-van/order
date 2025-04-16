package com.partridge.order.domain.product.search.service;

import org.springframework.stereotype.Service;

import com.partridge.order.domain.product.search.dto.ProductSearchDTO;

@Service
public class ProductSearchService {
	public ProductSearchDTO.ListResponse getProductList(ProductSearchDTO.ListRequest request) {
		return ProductSearchDTO.ListResponse.builder().build();
	}
}
