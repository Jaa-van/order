package com.partridge.order.domain.product.repository;

import java.util.List;

import com.partridge.order.domain.product.dto.ProductListDTO;

public interface ProductRepositoryCustom {
	List<ProductListDTO.ResponseProduct> getProductsByKeywordAndSortByOrder(ProductListDTO.Request request);
}
