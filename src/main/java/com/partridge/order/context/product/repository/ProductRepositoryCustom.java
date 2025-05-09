package com.partridge.order.context.product.repository;

import java.util.List;

import com.partridge.order.context.product.controller.dto.ProductListDTO;
import com.partridge.order.context.product.domain.model.Product;

public interface ProductRepositoryCustom {
	List<Product> getProductsByKeywordAndSortByOrder(ProductListDTO.Request request);
}
