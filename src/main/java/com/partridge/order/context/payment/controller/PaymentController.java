package com.partridge.order.context.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;
import com.partridge.order.context.payment.controller.dto.PaymentResponseMapper;
import com.partridge.order.context.payment.service.PaymentFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentFacade paymentFacade;
	private final PaymentResponseMapper paymentResponseMapper;

	@PostMapping("")
	public ResponseEntity<PaymentPostDTO.Response> postPayment(@Valid @RequestBody PaymentPostDTO.Request request) {
		return ResponseEntity.ok().body(paymentResponseMapper.toPaymentPostResponse(paymentFacade.postPayment(request)));
	}
}
