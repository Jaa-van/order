package com.partridge.order.context.order.service;

import static com.partridge.order.context.order.constant.OrderCommonCodeEnum.*;
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

import com.partridge.order.context.order.controller.dto.OrderPostDTO;
import com.partridge.order.context.order.exception.DuplicateOrderRequestException;
import com.partridge.order.context.order.exception.InventoryNotEnoughException;
import com.partridge.order.context.order.exception.OrderExpiredException;
import com.partridge.order.context.order.infra.OrderRedisUtil;
import com.partridge.order.context.order.repository.OrderProductRepotisory;
import com.partridge.order.context.order.repository.OrderRepository;
import com.partridge.order.context.order.domain.validator.OrderValidator;
import com.partridge.order.context.order.service.validator.OrderServiceValidator;
import com.partridge.order.context.product.repository.ProductRepository;
import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.product.domain.model.Product;
import com.partridge.order.context.product.service.ProductReader;
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
	@Mock
	private OrderServiceValidator mockedOrderServiceValidator;
	@Mock
	private ProductReader mockedProductReader;
	@Mock
	private OrderWriter mockedOrderWriter;

	@InjectMocks
	private OrderFacade orderFacade;
	@InjectMocks
	private OrderValidator orderValidator;
	@InjectMocks
	private OrderServiceValidator orderServiceValidator;

	@Test
	void postOrderKey_should_return_key() {
		//given
		String fakeKey = "fakeKey";
		try (MockedStatic<KeyUtil> mocked = mockStatic(KeyUtil.class)) {
			mocked.when(KeyUtil::generateKey).thenReturn(fakeKey);

			//when
			String key = orderFacade.postOrderKey();

			//then
			assertThat(key).isEqualTo(fakeKey);
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
		Order resposne = orderFacade.postOrder(request);

		//then
		verify(mockedProductRepository).findAllById(anyList());
		verify(mockedOrderServiceValidator).validateOrderKey(request.getKey());
		verify(mockedOrderValidator).validateProductInventory(eq(request), anyMap());
		verify(mockedOrderRedisUtil).setOrderProgress(request.getKey());
		verify(mockedOrderRepository).save(any(Order.class));
		verify(mockedOrderProductRepotisory, atLeastOnce()).save(any());

		assertThat(resposne.getKey()).isEqualTo(request.getKey());
		assertThat(resposne.getId()).isEqualTo(2L);
	}

	@Test
	void postOrder_should_throw_duplicate_order_request_exception_when_order_is_in_progress() {
		//given
		orderFacade = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();

		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_IN_PROGRESS.getCode());

		//when
		//then
		assertThrows(DuplicateOrderRequestException.class, () -> orderFacade.postOrder(request));
	}

	@Test
	void postOrder_should_throw_duplicate_oreder_request_exception_when_order_is_complete() {
		//given
		orderFacade = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();

		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_COMPLETE.getCode());

		//when
		//then
		assertThrows(DuplicateOrderRequestException.class, () -> orderFacade.postOrder(request));
	}

	@Test
	void postOrder_should_throw_order_expired_exception_when_order_key_is_expired() {
		//given
		orderFacade = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();

		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_EXPIRED.getCode());

		//when
		//then
		assertThrows(OrderExpiredException.class, () -> orderFacade.postOrder(request));
	}

	@Test
	void postOrder_should_throw_inventory_not_enough_exception_when_inventory_is_not_enough() {
		//given
		orderFacade = spy(mockOrderRedisUtilWithoutValidator());
		OrderPostDTO.Request request = fakeOrderRequestBuilder();
		List<Product> mockProducts = fakeInventoryNotEnoughProductListBuilder();

		when(mockedProductRepository.findAllById(anyList())).thenReturn(mockProducts);
		when(mockedOrderRedisUtil.getOrder(request.getKey())).thenReturn(ORDER_WAITING.getCode());

		//when
		//then
		assertThrows(InventoryNotEnoughException.class, () -> orderFacade.postOrder(request));
	}

	private OrderFacade mockOrderRedisUtilWithoutValidator() {
		return new OrderFacade(mockedOrderRedisUtil, orderServiceValidator, mockedProductReader, orderValidator,
			mockedOrderWriter);
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