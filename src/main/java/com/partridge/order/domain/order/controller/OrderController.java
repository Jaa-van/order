package com.partridge.order.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.domain.order.dto.OrderPostDTO;
import com.partridge.order.domain.order.dto.OrderPostKeyDTO;
import com.partridge.order.domain.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping("/key")
	public ResponseEntity<OrderPostKeyDTO.Resposne> postKey() {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.postOrderKey());
	}

	@PostMapping("")
	public ResponseEntity<OrderPostDTO.Response> postOrder(@Valid @RequestBody OrderPostDTO.Request request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.postOrder(request));
	}
}
