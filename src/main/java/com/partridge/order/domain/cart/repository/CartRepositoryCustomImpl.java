package com.partridge.order.domain.cart.repository;

import static com.partridge.order.global.entity.QCart.*;
import static com.partridge.order.global.entity.QProduct.*;

import java.util.List;

import com.partridge.order.domain.cart.dto.CartListDTO;
import com.partridge.order.domain.cart.dto.QCartListDTO_ResponseCart;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartRepositoryCustomImpl implements CartRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<CartListDTO.ResponseCart> getCartListByUserId(Long userId) {
		return queryFactory
			.select(new QCartListDTO_ResponseCart(cart.id.productId, cart.quantity, product.key, product.name,
				product.inventory, product.price, product.description))
			.from(cart)
			.join(product).on(cart.id.productId.eq(product.id))
			.where(cart.id.userId.eq(userId))
			.fetch();
	}
}
