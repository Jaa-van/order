package com.partridge.order.domain.payment.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.partridge.order.domain.order.redis.OrderRedisUtil;
import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.payment.gateway.PaymentGatewayClient;
import com.partridge.order.domain.payment.repository.PaymentRepository;
import com.partridge.order.domain.payment.validator.PaymentValidator;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
	@InjectMocks
	private PaymentService paymentService;

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private PaymentValidator paymentValidator;
	@Mock
	private PaymentRepository paymentRepository;
	@Mock
	private OrderRedisUtil orderRedisUtil;
	@Mock
	private PaymentGatewayClient paymentGatewayClient;

	@Test
	void postPayment_should_return_payment_post_response_when_payment_is_success() {
		//given

		//when

		//then
	}

	@Test
	void postPayment_should_throw_sold_out_exception_when_inventory_is_not_enough() {
		//given

		//when

		//then
	}

	@Test
	void postPayment_should_throw_duplicate_payment_exception_when_payment_is_in_progress() {
		//given

		//when

		//then
	}

	@Test
	void postPayment_should_throw_payment_already_complete_exception_when_payment_is_complete() {
		//given

		//when

		//then
	}

	@Test
	void postPayment_should_throw_payment_gateway_fail_exception_when_payment_is_failed() {
		//given

		//when

		//then
	}
}