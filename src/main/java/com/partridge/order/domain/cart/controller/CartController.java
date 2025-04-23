package com.partridge.order.domain.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.domain.cart.dto.CartListDTO;
import com.partridge.order.domain.cart.dto.CartPostDTO;
import com.partridge.order.domain.cart.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

	@GetMapping("/{userId}")
	public ResponseEntity<CartListDTO.Response> getCartList(@PathVariable Long userId) {
		return ResponseEntity.ok().body(cartService.getCartList(userId));
	}

	@PostMapping("")
	public ResponseEntity<Void> postCart(@Valid @RequestBody CartPostDTO.Request request) {
		cartService.postCart(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
