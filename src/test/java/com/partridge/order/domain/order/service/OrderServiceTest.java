package com.partridge.order.domain.order.service;

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
	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderRedisUtil orderRedisUtil;
	@Mock
	private OrderValidator orderValidator = new OrderValidator(orderRedisUtil);
	@Mock
	private ProductRepository productRepository;
	@Mock
	private OrderProductRepotisory orderProductRepotisory;

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
			verify(orderRedisUtil).setOrderWaiting(fakeKey);
		}
	}

	@Test
	void postOrder_should_return_post_response_when_post_order_success() {
		//given
		Long userId = 1L;
		String key = "fakeKey";
		List<OrderPostDTO.RequestProduct> products = List.of(new OrderPostDTO.RequestProduct(2L, 2L));
		OrderPostDTO.Request request = new OrderPostDTO.Request(userId, key, products);

		List<Product> mockProducts = List.of(Product.builder()
			.id(2L)
			.price(5000L)
			.inventory(2L)
			.name("product")
			.build());

		Order savedOrder = Order.builder()
			.id(2L)
			.userId(request.getUserId())
			.key(request.getKey())
			.totalPrice(10000L)
			.deliveryFee(false)
			.status("001")
			.build();

		when(productRepository.findAllById(
			request.getProducts().stream().map(OrderPostDTO.RequestProduct::getProductId).toList())).thenReturn(
			mockProducts);
		when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

		//when
		OrderPostDTO.Response resposne = orderService.postOrder(request);

		//then
		verify(productRepository).findAllById(anyList());
		verify(orderValidator).validateOrderKey(request.getKey());
		verify(orderValidator).validateProductInventory(eq(request), anyMap());
		verify(orderRedisUtil).setOrderProgress(request.getKey());
		verify(orderRepository).save(any(Order.class));
		verify(orderProductRepotisory, atLeastOnce()).save(any());

		assertThat(resposne.getKey()).isEqualTo(key);
		assertThat(resposne.getOrderId()).isEqualTo(2L);
	}

	@Test
	void postOrder_should_throw_duplicate_order_request_exception_when_order_key_is_duplicate() {
		//given
		Long userId = 1L;
		String key = "fakeDuplicateKey";
		List<OrderPostDTO.RequestProduct> products = List.of(new OrderPostDTO.RequestProduct(2L, 2L));
		OrderPostDTO.Request request = new OrderPostDTO.Request(userId, key, products);

		doThrow(new DuplicateOrderRequestException()).when(orderValidator).validateOrderKey(request.getKey());

		//when
		//then
		assertThrows(DuplicateOrderRequestException.class, () -> orderService.postOrder(request));
	}

	@Test
	void postOrder_should_throw_order_expired_exception_when_order_key_is_expired() {
		//given
		Long userId = 1L;
		String key = "fakeDuplicateKey";
		List<OrderPostDTO.RequestProduct> products = List.of(new OrderPostDTO.RequestProduct(2L, 2L));
		OrderPostDTO.Request request = new OrderPostDTO.Request(userId, key, products);

		doThrow(new OrderExpiredException()).when(orderValidator).validateOrderKey(request.getKey());

		//when
		//then
		assertThrows(OrderExpiredException.class, () -> orderService.postOrder(request));
	}

	@Test
	void postOrder_should_throw_inventory_not_enough_exception_when_inventory_is_not_enough() {
		//given
		Long userId = 1L;
		String key = "fakeDuplicateKey";
		List<OrderPostDTO.RequestProduct> products = List.of(new OrderPostDTO.RequestProduct(2L, 3L));
		OrderPostDTO.Request request = new OrderPostDTO.Request(userId, key, products);

		List<Product> mockProducts = List.of(Product.builder()
			.id(2L)
			.price(5000L)
			.inventory(2L)
			.name("product")
			.build());

		when(productRepository.findAllById(
			request.getProducts().stream().map(OrderPostDTO.RequestProduct::getProductId).toList())).thenReturn(
			mockProducts);

		doThrow(new InventoryNotEnoughException(mockProducts.get(0).getName())).when(orderValidator)
			.validateProductInventory(eq(request), anyMap());

		//when
		//then
		assertThrows(InventoryNotEnoughException.class, () -> orderService.postOrder(request));
	}
}