package com.partridge.order.context.product.repository;

import static com.partridge.order.context.product.domain.model.QProduct.*;

import java.util.List;

import com.partridge.order.context.product.controller.dto.ProductListDTO;
import com.partridge.order.context.product.domain.model.Product;
import com.partridge.order.context.product.repository.support.ProductPredicateBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Product> getProductsByKeywordAndSortByOrder(ProductListDTO.Request request) {
		return queryFactory
			.select(product)
			.from(product)
			.where(ProductPredicateBuilder.containKeywords(product, request.getKeyword(), request.getSearchType()))
			.orderBy(ProductPredicateBuilder.orderBy(product, request.getOrderBy(), request.getOrderDirection()))
			.fetch();
	}
}
