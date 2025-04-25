package com.partridge.order.domain.payment.service;

import static com.partridge.order.domain.payment.constant.PaymentCommonCode.*;
import static com.partridge.order.domain.payment.constant.PaymentConstantValue.*;
import static com.partridge.order.global.constant.ConstantValue.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.domain.order.redis.OrderRedisUtil;
import com.partridge.order.domain.order.repository.OrderProductRepotisory;
import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.payment.dto.PaymentGatewayDTO;
import com.partridge.order.domain.payment.dto.PaymentPostDTO;
import com.partridge.order.domain.payment.exception.PaymentGatewayFailException;
import com.partridge.order.domain.payment.gateway.PaymentGatewayClient;
import com.partridge.order.domain.payment.repository.PaymentRepository;
import com.partridge.order.domain.payment.validator.PaymentValidator;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.entity.Payment;
import com.partridge.order.global.exception.global.NotFoundException;
import com.partridge.order.global.lock.AcquireLock;
import com.partridge.order.global.lock.ReleaseLock;
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
			setProductInventory(productInventory);

			paymentGatewayClient.requestPayment(paymentGatewayRequestBuilder(request, getTotalPriceByOrderId(orderId)));
		} catch (PaymentGatewayFailException e) {
			postPaymentWithFailed(request);
			throw new PaymentGatewayFailException(e.getMessage());
		}

		return postResponseBuilder(payment);
	}

	private void postPaymentWithFailed(PaymentPostDTO.Request request) {
		paymentRepository.save(postRequestToFailedEntity(request));
	}

	private void setProductInventory(List<PaymentPostDTO.OrderProductInventory> productInventory) {
		productInventory.forEach(product -> {
			productRepository.updateProductInventory(product.getProductId(), product.getQuantity());
		});
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
