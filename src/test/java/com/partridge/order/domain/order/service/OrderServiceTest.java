package com.partridge.order.domain.order.service;

import static com.partridge.order.domain.order.constant.OrderCommonCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.partridge.order.domain.order.dto.OrderPostDTO;
import com.partridge.order.domain.order.dto.OrderPostKeyDTO;
import com.partridge.order.domain.order.exception.DuplicateOrderRequestException;
import com.partridge.order.domain.order.exception.InventoryNotEnoughException;
import com.partridge.order.domain.order.exception.OrderExpiredException;
import com.partridge.order.domain.order.redis.OrderRedisUtil;
import com.partridge.order.domain.order.repository.OrderProductRepotisory;
import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.order.validator.OrderValidator;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.entity.Order;
import com.partridge.order.global.entity.Product;
import com.partridge.order.global.util.KeyUtil;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private OrderRepository mockedOrderRepository;
	@Mock
	private OrderRedisUtil mockedOrderRedisUtil;
	@Mock
	private ProductRepository mockedProductRepository;
	@Mock
	private OrderProductRepotisory mockedOrderProductRepotisory;
	@Mock
	private OrderValidator mockedOrderValidator;

	@InjectMocks
	private OrderService orderService;
	@InjectMocks
	private OrderValidator orderValidator;

	@Test
	void postOrderKey_should_return_key() {
		//given
		String fakeKey = "fakeKey";
		try (MockedStatic<KeyUtil> mocked = mockStatic(KeyUtil.class)) {
			mocked.when(KeyUtil::generateKey).thenReturn(fakeKey);

			//when
			OrderPostKeyDTO.Resposne resposne = orderService.postOrderKey();

			//then
			assertThat(resposne.getKey()).isEqualTo(fakeKey);
			verify(mockedOrderRedisUtil).setOrderWaiting(fakeKey);
		}
	}

	@Test
	void postOrder_should_return_post_response_when_post_order_success() {
		//given
		OrderPostDTO.Request request = fakeOrderRequestBuilder();
		List<Product> mockProducts = fakeProductListBuilder();
		Order savedOrder = fakeOrderBuilder(request);

		when(mockedProductRepository.findAllById(
			request.getProducts().stream().map(OrderPostDTO.RequestProduct::getProductId).toList())).thenReturn(
			mockProducts);
		when(mockedOrderRepository.save(any(Order.class))).thenReturn(savedOrder);

		//when
		OrderPostDTO.Response resposne = orderService.postOrder(request);

		//then
		verify(mockedProductRepository).findAllById(anyList());
		verify(mockedOrderValidator).validateOrderKey(request.getKey());
		verify(mockedOrderValidator).validateProductInventory(eq(request), anyMap());
		verify(mockedOrderRedisUtil).setOrderProgress(request.getKey());
		verify(mockedOrderRepository).save(any(Order.class));
		verify(mockedOrderProductRepotisory, atLeastOnce()).save(any());

		assertThat(resposne.getKey()).isEqualTo(request.getKey());
		assertThat(resposne.getOrderId()).isEqualTo(2L);
	}

	@Test
	void postOrder_should_throw_duplicate_order_request_exception_when_order_is_in_progress() {
		//given
		orderService = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();

		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_IN_PROGRESS);

		//when
		//then
		assertThrows(DuplicateOrderRequestException.class, () -> orderService.postOrder(request));
	}

	@Test
	void postOrder_should_throw_duplicate_oreder_request_exception_when_order_is_complete() {
		//given
		orderService = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();

		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_COMPLETE);

		//when
		//then
		assertThrows(DuplicateOrderRequestException.class, () -> orderService.postOrder(request));
	}

	@Test
	void postOrder_should_throw_order_expired_exception_when_order_key_is_expired() {
		//given
		orderService = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();

		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_EXPIRED);

		//when
		//then
		assertThrows(OrderExpiredException.class, () -> orderService.postOrder(request));
	}

	@Test
	void postOrder_should_throw_inventory_not_enough_exception_when_inventory_is_not_enough() {
		//given
		orderService = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();
		List<Product> mockProducts = fakeInventoryNotEnoughProductListBuilder();

		when(mockedProductRepository.findAllById(anyList())).thenReturn(mockProducts);
		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_WAITING);

		//when
		//then
		assertThrows(InventoryNotEnoughException.class, () -> orderService.postOrder(request));
	}

	private OrderService mockOrderRedisUtilWithoutValidator() {
		return new OrderService(mockedOrderRepository, mockedProductRepository, mockedOrderProductRepotisory, mockedOrderRedisUtil,
			orderValidator);
	}

	private OrderPostDTO.Request fakeOrderRequestBuilder() {
		Long userId = 1L;
		String key = "fakeDuplicateKey";
		List<OrderPostDTO.RequestProduct> products = List.of(OrderPostDTO.RequestProduct.builder()
				.productId(2L)
				.quantity(3L)
			.build());
		return OrderPostDTO.Request.builder()
			.userId(userId)
			.key(key)
			.products(products)
			.build();
	}

	private List<Product> fakeProductListBuilder() {
		return List.of(Product.builder()
			.id(2L)
			.price(5000L)
			.inventory(4L)
			.name("product")
			.build());
	}

	private List<Product> fakeInventoryNotEnoughProductListBuilder() {
		return List.of(Product.builder()
			.id(2L)
			.price(5000L)
			.inventory(2L)
			.name("product")
			.build());
	}

	private Order fakeOrderBuilder(OrderPostDTO.Request request) {
		return Order.builder()
			.id(2L)
			.userId(request.getUserId())
			.key(request.getKey())
			.totalPrice(10000L)
			.deliveryFee(false)
			.status("001")
			.build();
	}

}