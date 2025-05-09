package com.partridge.order.context.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.context.order.controller.dto.OrderListDto;
import com.partridge.order.context.order.controller.dto.OrderResponseMapper;
import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.controller.dto.OrderPostKeyDto;
import com.partridge.order.context.order.service.OrderFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {
	private final OrderResponseMapper orderResponseMapper;
	private final OrderFacade orderFacade;

	@PostMapping("/api/v1/orders/key")
	public ResponseEntity<OrderPostKeyDto.Resposne> postKey() {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(orderResponseMapper.toOrderPostKeyResponse(orderFacade.postOrderKey()));
	}

	@PostMapping("/api/v1/orders")
	public ResponseEntity<OrderPostDto.Response> postOrder(@Valid @RequestBody OrderPostDto.Request request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(orderResponseMapper.toOrderPostResponse(orderFacade.postOrder(request)));
	}

	@GetMapping("/api/v1/orders/{userId}")
	public ResponseEntity<OrderListDto.Response> getOrderList(@PathVariable Long userId) {
		return ResponseEntity.ok().body(orderResponseMapper.toOrderListResponse(orderFacade.getOrderList(userId)));
	}
}
