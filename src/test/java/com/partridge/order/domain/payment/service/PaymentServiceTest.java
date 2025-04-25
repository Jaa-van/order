package com.partridge.order.domain.payment.service;

import static com.partridge.order.domain.payment.constant.PaymentCommonCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.partridge.order.domain.order.redis.OrderRedisUtil;
import com.partridge.order.domain.order.repository.OrderProductRepotisory;
import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.payment.dto.PaymentPostDTO;
import com.partridge.order.domain.payment.exception.DuplicatePaymentException;
import com.partridge.order.domain.payment.exception.PaymentAlreadyCompleteException;
import com.partridge.order.domain.payment.exception.PaymentGatewayFailException;
import com.partridge.order.domain.payment.exception.SoldOutException;
import com.partridge.order.domain.payment.gateway.PaymentGatewayClient;
import com.partridge.order.domain.payment.repository.PaymentRepository;
import com.partridge.order.domain.payment.validator.PaymentValidator;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.entity.Order;
import com.partridge.order.global.entity.Payment;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
	@InjectMocks
	private PaymentService paymentService;
	@InjectMocks
	private PaymentValidator paymentValidator;

	@Mock
	private OrderRepository mockedOrderRepository;
	@Mock
	private PaymentValidator mockedPaymentValidator;
	@Mock
	private PaymentRepository mockedPaymentRepository;
	@Mock
	private OrderRedisUtil mockedOrderRedisUtil;
	@Mock
	private PaymentGatewayClient mockedPaymentGatewayClient;
	@Mock
	private ProductRepository mockedProductRepository;
	@Mock
	private OrderProductRepotisory mockedOrderProductRepotisory;

	@Test
	void postPayment_should_return_payment_post_response_when_payment_is_success() {
		//given
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<PaymentPostDTO.OrderProductInventory> orderProduct = fakeOrderProductInventoryBuilder();
		Order order = fakeOrderBuilder(request);
		Payment expectedPayment = fakePayment(request);

		when(mockedOrderProductRepotisory.getInventoryByOrderId(request.getOrderId())).thenReturn(orderProduct);
		when(mockedOrderRepository.findById(request.getOrderId())).thenReturn(Optional.ofNullable(order));
		when(mockedPaymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

		//when
		PaymentPostDTO.Response response = paymentService.postPayment(request);

		//then
		assertThat(response).isNotNull();
		assertThat(response.getOrderId()).isEqualTo(request.getOrderId());
		assertThat(response.getPaymentId()).isNotNull();
		assertThat(response.getStatus()).isEqualTo(expectedPayment.getStatus());
	}

	@Test
	void postPayment_should_throw_sold_out_exception_when_inventory_is_not_enough() {
		//given
		paymentService = spy(spyPaymentValidator());
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<PaymentPostDTO.OrderProductInventory> orderProduct = fakeSoldoutOrderProductInventoryBuilder();

		when(mockedOrderProductRepotisory.getInventoryByOrderId(request.getOrderId())).thenReturn(orderProduct);

		//when
		//then
		assertThrows(SoldOutException.class, () -> paymentService.postPayment(request));
	}

	@Test
	void postPayment_should_throw_duplicate_payment_exception_when_payment_is_in_progress() {
		//given
		paymentService = spy(spyPaymentValidator());
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<PaymentPostDTO.OrderProductInventory> orderProduct = fakeOrderProductInventoryBuilder();

		when(mockedPaymentRepository.existsPaymentByOrderId(request.getOrderId(), PAYMENT_IN_PROGRESS)).thenReturn(
			true);

		//when
		//then
		assertThrows(DuplicatePaymentException.class, () -> paymentService.postPayment(request));
	}

	@Test
	void postPayment_should_throw_payment_already_complete_exception_when_payment_is_complete() {
		//given
		paymentService = spy(spyPaymentValidator());
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<PaymentPostDTO.OrderProductInventory> orderProduct = fakeOrderProductInventoryBuilder();

		when(mockedPaymentRepository.existsPaymentByOrderId(request.getOrderId(), PAYMENT_IN_PROGRESS)).thenReturn(
			false);
		when(mockedPaymentRepository.existsPaymentByOrderId(request.getOrderId(), PAYMENT_COMPLETE)).thenReturn(
			true);

		//when
		//then
		assertThrows(PaymentAlreadyCompleteException.class, () -> paymentService.postPayment(request));
	}

	@Test
	void postPayment_should_throw_payment_gateway_fail_exception_when_payment_is_failed() {
		//given
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		Order order = fakeOrderBuilder(request);

		when(mockedOrderRepository.findById(request.getOrderId())).thenReturn(Optional.ofNullable(order));
		doThrow(new PaymentGatewayFailException(request.getKey())).when(mockedPaymentGatewayClient)
			.requestPayment(any());

		//when
		//then
		assertThrows(PaymentGatewayFailException.class, () -> paymentService.postPayment(request));
	}

	private PaymentService spyPaymentValidator() {
		return new PaymentService(mockedPaymentRepository, mockedPaymentGatewayClient, mockedOrderRepository,
			mockedProductRepository, mockedOrderProductRepotisory, paymentValidator, mockedOrderRedisUtil);
	}

	private PaymentPostDTO.Request fakePaymentPostRequestBuilder() {
		Long orderId = 1L;
		String key = "fakeKey";
		String method = "fakeMethod";
		return PaymentPostDTO.Request.builder()
			.orderId(orderId)
			.key(key)
			.method(method)
			.build();
	}

	private List<PaymentPostDTO.OrderProductInventory> fakeOrderProductInventoryBuilder() {
		return List.of(PaymentPostDTO.OrderProductInventory.builder()
			.orderId(2L)
			.productId(1L)
			.quantity(2L)
			.name("product")
			.inventory(3L)
			.build());
	}

	private List<PaymentPostDTO.OrderProductInventory> fakeSoldoutOrderProductInventoryBuilder() {
		return List.of(PaymentPostDTO.OrderProductInventory.builder()
			.orderId(2L)
			.productId(1L)
			.quantity(4L)
			.name("product")
			.inventory(3L)
			.build());
	}

	private Order fakeOrderBuilder(PaymentPostDTO.Request request) {
		return Order.builder()
			.id(request.getOrderId())
			.key(request.getKey())
			.userId(1L)
			.totalPrice(5000L)
			.deliveryFee(true)
			.status("002")
			.build();
	}

	private Payment fakePayment(PaymentPostDTO.Request request) {
		return Payment.builder()
			.id(4L)
			.orderId(request.getOrderId())
			.status("003")
			.method(request.getMethod())
			.build();
	}
}