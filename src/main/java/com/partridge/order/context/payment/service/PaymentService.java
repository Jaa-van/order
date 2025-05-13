package com.partridge.order.context.payment.service;

import static com.partridge.order.context.payment.constant.PaymentCommonCode.*;
import static com.partridge.order.global.constant.ConstantValue.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.context.order.infra.OrderRedisUtil;
import com.partridge.order.context.order.repository.OrderProductRepotisory;
import com.partridge.order.context.order.repository.OrderRepository;
import com.partridge.order.context.payment.controller.dto.PaymentGatewayDTO;
import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;
import com.partridge.order.context.payment.exception.PaymentGatewayFailException;
import com.partridge.order.context.payment.infra.payment.PaymentGatewayClient;
import com.partridge.order.context.payment.repository.PaymentRepository;
import com.partridge.order.context.payment.validator.PaymentValidator;
import com.partridge.order.context.product.repository.ProductRepository;
import com.partridge.order.context.payment.domain.model.Payment;
import com.partridge.order.global.exception.global.NotFoundException;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
	private final PaymentRepository paymentRepository;
	private final PaymentGatewayClient paymentGatewayClient;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final OrderProductRepotisory orderProductRepotisory;
	private final PaymentValidator paymentValidator;
	private final OrderRedisUtil orderRedisUtil;

	@Log
	@Transactional(rollbackFor = Exception.class)
	// @AcquireLock(lockName = PAYMENT_LOCK_NAME)
	// @ReleaseLock(lockName = PAYMENT_LOCK_NAME)
	public PaymentPostDTO.Response postPayment(PaymentPostDTO.Request request) {
		Long orderId = request.getOrderId();
		List<PaymentPostDTO.OrderProductInventory> productInventory = orderProductRepotisory.getInventoryByOrderId(
			orderId);
		paymentValidator.validateProductInventory(productInventory);
		paymentValidator.validateDuplicatePayment(orderId);

		Payment payment;
		try {
			payment = paymentRepository.save(postRequestToCompleteEntity(request));
			orderRepository.updateStatusByOrderId(orderId, PAYMENT_COMPLETE);
			orderRedisUtil.setOrderComplete(request.getKey());
			productInventory.forEach(
				product -> productRepository.updateProductInventory(product.getProductId(), product.getQuantity()));

			paymentGatewayClient.requestPayment(paymentGatewayRequestBuilder(request, getTotalPriceByOrderId(orderId)));
		} catch (PaymentGatewayFailException e) {
			postPaymentWithFailed(request);
			orderRedisUtil.setOrderWaiting(request.getKey());
			throw new PaymentGatewayFailException(e.getMessage());
		}

		return postResponseBuilder(payment);
	}

	private void postPaymentWithFailed(PaymentPostDTO.Request request) {
		paymentRepository.save(postRequestToFailedEntity(request));
	}

	private Payment postRequestToCompleteEntity(PaymentPostDTO.Request request) {
		return Payment.builder()
			.orderId(request.getOrderId())
			.method(request.getMethod())
			.status(PAYMENT_COMPLETE)
			.build();
	}

	private Payment postRequestToFailedEntity(PaymentPostDTO.Request request) {
		return Payment.builder()
			.orderId(request.getOrderId())
			.method(request.getMethod())
			.status(PAYMENT_FAILED)
			.build();
	}

	public PaymentGatewayDTO.Request paymentGatewayRequestBuilder(PaymentPostDTO.Request request, Long totalPrice) {
		return PaymentGatewayDTO.Request.builder()
			.method(request.getMethod())
			.key(request.getKey())
			.price(totalPrice)
			.build();
	}


	private PaymentPostDTO.Response postResponseBuilder(Payment payment) {
		return PaymentPostDTO.Response.builder()
			.paymentId(payment.getId())
			.orderId(payment.getOrderId())
			.method(payment.getMethod())
			.status(payment.getStatus())
			.build();
	}

	public Long getTotalPriceByOrderId(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException(ORDER_KR))
			.getTotalPrice();
	}
}
