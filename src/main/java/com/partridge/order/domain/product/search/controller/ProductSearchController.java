package com.partridge.order.domain.product.search.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.domain.product.search.dto.ProductSearchDTO;
import com.partridge.order.domain.product.search.service.ProductSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/search")
@RequiredArgsConstructor
public class ProductSearchController {
	private final ProductSearchService productSearchService;

	@RequestMapping("")
	public ResponseEntity<ProductSearchDTO.ListResponse> getProductList(ProductSearchDTO.ListRequest request) {
		return ResponseEntity.ok().body(productSearchService.getProductList(request));
	}
}
