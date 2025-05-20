package com.partridge.order.context.payment.service;

import static com.partridge.order.context.payment.constant.PaymentCommonCode.*;
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

import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.order.infra.OrderRedisUtil;
import com.partridge.order.context.order.service.OrderReader;
import com.partridge.order.context.order.service.OrderWriter;
import com.partridge.order.context.order.service.dto.OrderProductInventory;
import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;
import com.partridge.order.context.payment.domain.model.Payment;
import com.partridge.order.context.payment.exception.DuplicatePaymentException;
import com.partridge.order.context.payment.exception.PaymentAlreadyCompleteException;
import com.partridge.order.context.payment.exception.PaymentGatewayFailException;
import com.partridge.order.context.payment.exception.SoldOutException;
import com.partridge.order.context.payment.infra.dto.PaymentGatewayMapper;
import com.partridge.order.context.payment.infra.payment.PaymentGatewayClient;
import com.partridge.order.context.payment.repository.PaymentRepository;
import com.partridge.order.context.payment.service.dto.PaymentServiceMapper;
import com.partridge.order.context.payment.service.validator.PaymentServiceValidator;
import com.partridge.order.context.product.service.ProductWriter;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
	@InjectMocks
	private PaymentFacade paymentFacade;
	@InjectMocks
	private PaymentServiceValidator paymentServiceValidator;

	@Mock
	private PaymentServiceValidator mockedPaymentServiceValidator;
	@Mock
	private OrderReader mockedOrderReader;
	@Mock
	private OrderWriter mockedOrderWriter;
	@Mock
	private PaymentWriter mockedPaymentWriter;
	@Mock
	private ProductWriter mockedProductWriter;
	@Mock
	private OrderRedisUtil mockedOrderRedisUtil;
	@Mock
	private PaymentGatewayClient mockedPaymentGatewayClient;
	@Mock
	private PaymentReader mockedPaymentReader;
	@Mock
	private PaymentServiceMapper mockedPaymentServiceMapper;
	@Mock
	private PaymentGatewayMapper mockedPaymentGatewayMapper;

	private final PaymentServiceMapper paymentServiceMapper = new PaymentServiceMapper();
	private final PaymentGatewayMapper paymentGatewayMapper = new PaymentGatewayMapper();

	@Test
	void postPayment_should_return_payment_post_response_when_payment_is_success() {
		//given
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<OrderProductInventory> orderProduct = fakeOrderProductInventoryBuilder();
		Order order = fakeOrderBuilder(request);
		Payment expectedPayment = fakePayment(request);

		when(mockedOrderReader.getProductInventoryListByOrderId(request.getOrderId())).thenReturn(orderProduct);
		when(mockedPaymentServiceMapper.toEntityWithPaymentComplete(request)).thenReturn(expectedPayment);
		when(mockedPaymentWriter.postPayment(any(Payment.class))).thenReturn(expectedPayment);

		//when
		Payment payment = paymentFacade.postPayment(request);

		//then
		assertThat(payment).isNotNull();
		assertThat(payment.getOrderId()).isEqualTo(request.getOrderId());
		assertThat(payment.getId()).isNotNull();
		assertThat(payment.getStatus()).isEqualTo(expectedPayment.getStatus());
	}

	@Test
	void postPayment_should_throw_sold_out_exception_when_inventory_is_not_enough() {
		//given
		paymentFacade = spy(spyPaymentValidator());
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<OrderProductInventory> orderProduct = fakeSoldoutOrderProductInventoryBuilder();

		when(mockedOrderReader.getProductInventoryListByOrderId(request.getOrderId())).thenReturn(orderProduct);

		//when
		//then
		assertThrows(SoldOutException.class, () -> paymentFacade.postPayment(request));
	}

	@Test
	void postPayment_should_throw_duplicate_payment_exception_when_payment_is_in_progress() {
		//given
		paymentFacade = spy(spyPaymentValidator());
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<OrderProductInventory> orderProduct = fakeOrderProductInventoryBuilder();

		when(mockedPaymentReader.existsPaymentByOrderIdAndStatus(request.getOrderId(), PAYMENT_IN_PROGRESS)).thenReturn(
			true);

		//when
		//then
		assertThrows(DuplicatePaymentException.class, () -> paymentFacade.postPayment(request));
	}

	@Test
	void postPayment_should_throw_payment_already_complete_exception_when_payment_is_complete() {
		//given
		paymentFacade = spy(spyPaymentValidator());
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		List<OrderProductInventory> orderProduct = fakeOrderProductInventoryBuilder();

		when(mockedPaymentReader.existsPaymentByOrderIdAndStatus(request.getOrderId(), PAYMENT_IN_PROGRESS)).thenReturn(
			false);
		when(mockedPaymentReader.existsPaymentByOrderIdAndStatus(request.getOrderId(), PAYMENT_COMPLETE)).thenReturn(
			true);

		//when
		//then
		assertThrows(PaymentAlreadyCompleteException.class, () -> paymentFacade.postPayment(request));
	}

	@Test
	void postPayment_should_throw_payment_gateway_fail_exception_when_payment_is_failed() {
		//given
		PaymentPostDTO.Request request = fakePaymentPostRequestBuilder();
		Order order = fakeOrderBuilder(request);
		Payment expectedPayment = fakePayment(request);

		when(mockedPaymentServiceMapper.toEntityWithPaymentComplete(request)).thenReturn(expectedPayment);
		doThrow(new PaymentGatewayFailException(request.getKey())).when(mockedPaymentGatewayClient)
			.requestPayment(any());

		//when
		//then
		assertThrows(PaymentGatewayFailException.class, () -> paymentFacade.postPayment(request));
	}

	private PaymentFacade spyPaymentValidator() {
		return new PaymentFacade(mockedOrderReader, mockedOrderWriter, mockedPaymentWriter, mockedProductWriter,
			mockedOrderRedisUtil, paymentServiceValidator, mockedPaymentGatewayClient, paymentServiceMapper,
			paymentGatewayMapper);
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

	private List<OrderProductInventory> fakeOrderProductInventoryBuilder() {
		return List.of(OrderProductInventory.builder()
			.orderId(2L)
			.productId(1L)
			.quantity(2L)
			.name("product")
			.inventory(3L)
			.build());
	}

	private List<OrderProductInventory> fakeSoldoutOrderProductInventoryBuilder() {
		return List.of(OrderProductInventory.builder()
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