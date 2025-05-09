package com.partridge.order.context.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.context.order.controller.dto.OrderResponseMapper;
import com.partridge.order.context.order.controller.dto.OrderPostDTO;
import com.partridge.order.context.order.controller.dto.OrderPostKeyDTO;
import com.partridge.order.context.order.service.OrderFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderResponseMapper orderResponseMapper;
	private final OrderFacade orderFacade;

	@PostMapping("/key")
	public ResponseEntity<OrderPostKeyDTO.Resposne> postKey() {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(orderResponseMapper.toOrderPostKeyResponse(orderFacade.postOrderKey()));
	}

	@PostMapping("")
	public ResponseEntity<OrderPostDTO.Response> postOrder(@Valid @RequestBody OrderPostDTO.Request request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(orderResponseMapper.toOrderPostResponse(orderFacade.postOrder(request)));
	}
}
