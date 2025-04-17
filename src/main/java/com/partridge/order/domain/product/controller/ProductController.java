package com.partridge.order.domain.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.domain.product.dto.ProductDetailDTO;
import com.partridge.order.domain.product.dto.ProductListDTO;
import com.partridge.order.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDetailDTO.Response> getProduct(@PathVariable Long productId) {
		return ResponseEntity.ok().body(productService.getProduct(productId));
	}

	@GetMapping("")
	public ResponseEntity<ProductListDTO.Response> getProducts(ProductListDTO.Request request) {
		return ResponseEntity.ok().body(productService.getProducts(request));
	}
}
