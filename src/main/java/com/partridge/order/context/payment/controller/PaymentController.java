package com.partridge.order.context.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.context.payment.dto.PaymentPostDTO;
import com.partridge.order.context.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping("")
	public ResponseEntity<PaymentPostDTO.Response> postPayment(@Valid @RequestBody PaymentPostDTO.Request request) {
		return ResponseEntity.ok().body(paymentService.postPayment(request));
	}
}
