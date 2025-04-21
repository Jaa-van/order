package com.partridge.order.domain.payment.service;

import static com.partridge.order.domain.payment.constant.PaymentCommonCode.*;
import static com.partridge.order.global.constant.ConstantValue.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.domain.order.repository.OrderProductRepotisory;
import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.payment.dto.PaymentGatewayDTO;
import com.partridge.order.domain.payment.dto.PaymentPostDTO;
import com.partridge.order.domain.payment.exception.SoldOutException;
import com.partridge.order.domain.payment.gateway.PaymentGatewayClient;
import com.partridge.order.domain.payment.repository.PaymentRepository;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.constant.ConstantValue;
import com.partridge.order.global.entity.Order;
import com.partridge.order.global.entity.OrderProduct;
import com.partridge.order.global.entity.Payment;
import com.partridge.order.global.exception.businessExceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentRepository paymentRepository;
	private final PaymentGatewayClient paymentGatewayClient;
	private final OrderRepository orderRepository;
	private final OrderProductRepotisory orderProductRepotisory;
	private final ProductRepository productRepository;

	@Transactional
	public PaymentPostDTO.Response postPayment(PaymentPostDTO.Request request) {
		paymentRepository.save(postRequestToEntity(request));

		orderProductRepotisory.getInventoryByOrderId(request.getOrderId())
			.forEach(orderProductInventory -> {
				if (orderProductInventory.getQuantity() > orderProductInventory.getInventory()) {
					throw new SoldOutException(orderProductInventory.getName());
				}
			});

		paymentGatewayClient.requestPayment(paymentGatewayRequestBuilder(request,
			orderRepository.findById(request.getOrderId())
				.orElseThrow(() -> new NotFoundException(PRODUCT))
				.getTotalPrice()));

		return PaymentPostDTO.Response.builder().success("yes").build();
	}

	private Payment postRequestToEntity(PaymentPostDTO.Request request) {
		return Payment.builder()
			.orderId(request.getOrderId())
			.method(request.getMethod())
			.status(PAYMENT_IN_PROGRESS)
			.build();
	}

	private PaymentGatewayDTO.Request paymentGatewayRequestBuilder(PaymentPostDTO.Request request, Long totalPrice) {
		return PaymentGatewayDTO.Request.builder()
			.method(request.getMethod())
			.key(request.getKey())
			.price(totalPrice)
			.build();
	}
}
