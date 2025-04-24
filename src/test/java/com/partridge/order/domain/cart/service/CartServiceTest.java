package com.partridge.order.domain.cart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import com.partridge.order.domain.cart.dto.CartListDTO;
import com.partridge.order.domain.cart.dto.CartPostDTO;
import com.partridge.order.domain.cart.repository.CartRepository;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.entity.Product;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
	@InjectMocks
	private CartService cartService;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ProductRepository productRepository;

	@Test
	void getCartList_should_return_cart_list_response_when_user_id_has_cart_list() {
		// given
		Long userId = 1L;
		List<CartListDTO.ResponseCart> mockCartList = List.of(
			CartListDTO.ResponseCart.builder()
				.productId(2L)
				.quantity(1L)
				.name("product")
				.key("lkwjbwjirflwkfj")
				.price(4000L)
				.description("description")
				.build()
		);

		when(cartRepository.getCartListByUserId(userId)).thenReturn(mockCartList);

		// when
		CartListDTO.Response response = cartService.getCartList(userId);

		// then
		assertThat(response.getCarts()).hasSize(1);
		assertThat(response.getCarts().get(0).getProductId()).isEqualTo(2L);
		assertThat(response.getCarts().get(0).getQuantity()).isEqualTo(1L);
		assertThat(response.getCarts().get(0).getName()).isEqualTo("product");
		assertThat(response.getCarts().get(0).getKey()).isEqualTo("lkwjbwjirflwkfj");
		assertThat(response.getCarts().get(0).getPrice()).isEqualTo(4000L);
		assertThat(response.getCarts().get(0).getDescription()).isEqualTo("description");
	}

	@Test
	void postCart_should_return_void_when_post_cart_success() {
		//given
		Long userId = 1L;
		List<CartPostDTO.RequestCart> mockCartList = List.of(new CartPostDTO.RequestCart(2L, 3L));
		CartPostDTO.Request mockRequest = new CartPostDTO.Request(userId, mockCartList);

		when(productRepository.findAllById(List.of(2L))).thenReturn(
			List.of(Product.builder().id(2L).inventory(3L).build()));

		//when
		cartService.postCart(mockRequest);

		//then
		verify(cartRepository).upsertCart(userId, 2L, 3L);
	}
}