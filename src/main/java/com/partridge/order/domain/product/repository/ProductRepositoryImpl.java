package com.partridge.order.domain.product.repository;

import static com.partridge.order.global.entity.QProduct.*;

import java.util.List;

import com.partridge.order.domain.product.dto.ProductListDTO;
import com.partridge.order.domain.product.dto.QProductListDTO_ResponseProduct;
import com.partridge.order.domain.product.repository.support.ProductPredicateBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<ProductListDTO.ResponseProduct> getProductsByKeywordAndSortByOrder(ProductListDTO.Request request) {
		return queryFactory
				.select(new QProductListDTO_ResponseProduct(product.id.longValue(), product.key, product.name,
					product.inventory, product.price, product.description))
				.from(product)
				.where(ProductPredicateBuilder.containKeywords(product, request.getKeyword(), request.getSearchType()))
				.orderBy(ProductPredicateBuilder.orderBy(product, request.getOrderBy(), request.getOrderDirection()))
				.fetch();
	}
}
