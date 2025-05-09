package com.partridge.order.context.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.context.product.controller.dto.ProductDetailDTO;
import com.partridge.order.context.product.controller.dto.ProductListDTO;
import com.partridge.order.context.product.controller.dto.ProductPostDTO;
import com.partridge.order.context.product.controller.dto.ProductResponseMapper;
import com.partridge.order.context.product.service.ProductFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductFacade productFacade;
	private final ProductResponseMapper productResponseMapper;

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDetailDTO.Response> getProductDetail(@PathVariable Long productId) {
		return ResponseEntity.ok()
			.body(productResponseMapper.toProductDetailResponse(productFacade.getProductDetail(productId)));
	}

	@GetMapping("")
	public ResponseEntity<ProductListDTO.Response> getProductList(@Valid ProductListDTO.Request request) {
		return ResponseEntity.ok()
			.body(productResponseMapper.toProductListResponse(productFacade.getProductList(request)));
	}

	@PostMapping("")
	public ResponseEntity<ProductPostDTO.Response> postProduct(@Valid @RequestBody ProductPostDTO.Request request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(productResponseMapper.toProductPostResponse(productFacade.postProduct(request)));
	}
}
