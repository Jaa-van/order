package com.partridge.order.domain.cart.repository;

import java.util.List;

import com.partridge.order.domain.cart.dto.CartListDTO;

public interface CartRepositoryCustom {
	List<CartListDTO.ResponseCart> getCartListByUserId(Long userId);
}
