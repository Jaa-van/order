package com.partridge.order.domain.cart.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.domain.cart.dto.CartListDTO;
import com.partridge.order.domain.cart.dto.CartPostDTO;
import com.partridge.order.domain.cart.repository.CartRepository;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;

	public CartListDTO.Response getCartList(Long userId) {
		return Optional.of(cartRepository.getCartListByUserId(userId))
			.map(this::cartListResponseBuilder)
			.orElse(null);
	}

	@Transactional
	public void postCart(CartPostDTO.Request request) {
		Map<Long, Long> inventoryMap = productRepository.findAllById(request.getCarts().stream()
			.map(CartPostDTO.RequestCart::getProductId)
			.toList()).stream().collect(Collectors.toMap(Product::getId, Product::getInventory));

		request.getCarts()
			.forEach(cart -> {
				if (inventoryMap.get(cart.getProductId()) <= cart.getQuantity()) {
					cartRepository.upsertCart(request.getUserId(), cart.getProductId(),	inventoryMap.get(cart.getProductId()));
				} else {
					cartRepository.upsertCart(request.getUserId(), cart.getProductId(), cart.getQuantity());
				}
			});
	}

	private CartListDTO.Response cartListResponseBuilder(List<CartListDTO.ResponseCart> cartList) {
		return CartListDTO.Response.builder()
			.carts(cartList)
			.totalCount(cartList.size())
			.build();
	}
}
