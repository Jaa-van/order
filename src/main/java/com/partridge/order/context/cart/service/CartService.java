package com.partridge.order.context.cart.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.context.cart.dto.CartListDTO;
import com.partridge.order.context.cart.dto.CartPostDTO;
import com.partridge.order.context.cart.repository.CartRepository;
import com.partridge.order.context.product.repository.ProductRepository;
import com.partridge.order.context.product.domain.model.Product;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;

	@Log
	public CartListDTO.Response getCartList(Long userId) {
		return Optional.of(cartRepository.getCartListByUserId(userId))
			.map(this::cartListResponseBuilder)
			.orElse(null);
	}

	@Log
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
