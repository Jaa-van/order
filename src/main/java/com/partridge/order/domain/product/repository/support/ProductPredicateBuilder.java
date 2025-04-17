package com.partridge.order.domain.product.repository.support;

import com.partridge.order.global.constant.ConstantValue;
import com.partridge.order.global.entity.QProduct;
import com.partridge.order.global.util.TextUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public class ProductPredicateBuilder {
	public static Predicate containKeywords(QProduct product, String keyword, String searchType) {
		if (TextUtil.isNullOrEmpty(keyword) || TextUtil.isNullOrEmpty(searchType)) {
			return null;
		}

		return switch (searchType) {
			case "name" -> product.name.containsIgnoreCase(keyword);
			case "description" -> product.description.containsIgnoreCase(keyword);
			default -> null;
		};
	}

	public static OrderSpecifier<?> orderBy(QProduct product, String orderBy, String orderDirection) {
		if (TextUtil.isNullOrEmpty(orderBy) || TextUtil.isNullOrEmpty(orderDirection)) {
			return product.price.asc();
		}

		Order order = orderDirection.equals(ConstantValue.ASC) ? Order.ASC : Order.DESC;

		return switch (orderBy) {
			case "name" -> new OrderSpecifier<>(order, product.name);
			case "price" -> new OrderSpecifier<>(order, product.price);
			case "quantity" -> new OrderSpecifier<>(order, product.quantity);
			default -> product.price.asc();
		};
	}
}
