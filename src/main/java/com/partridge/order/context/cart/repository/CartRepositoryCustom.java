package com.partridge.order.context.cart.repository;

import java.util.List;

import com.partridge.order.context.cart.dto.CartListDTO;

public interface CartRepositoryCustom {
	List<CartListDTO.ResponseCart> getCartListByUserId(Long userId);
}
