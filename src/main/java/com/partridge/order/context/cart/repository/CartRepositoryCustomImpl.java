package com.partridge.order.context.cart.repository;

import static com.partridge.order.context.product.domain.model.QProduct.*;
import static com.partridge.order.global.entity.QCart.*;

import java.util.List;

import com.partridge.order.context.cart.dto.CartListDTO;
import com.partridge.order.context.cart.dto.QCartListDTO_ResponseCart;
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
