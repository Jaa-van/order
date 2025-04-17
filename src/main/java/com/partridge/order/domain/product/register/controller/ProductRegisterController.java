package com.partridge.order.domain.product.register.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.domain.product.dto.ProductRegisterDTO;
import com.partridge.order.domain.product.register.service.ProductRegisterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/register")
@RequiredArgsConstructor
public class ProductRegisterController {
	private final ProductRegisterService productRegisterService;

	@PostMapping()
	public ResponseEntity<ProductRegisterDTO.Response> postProduct(@RequestBody ProductRegisterDTO.Request request) {
		return ResponseEntity.ok().body(productRegisterService.postProduct(request));
	}
}
