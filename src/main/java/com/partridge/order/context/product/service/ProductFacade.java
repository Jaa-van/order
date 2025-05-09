package com.partridge.order.context.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.partridge.order.context.product.controller.dto.ProductListDTO;
import com.partridge.order.context.product.controller.dto.ProductPostDTO;
import com.partridge.order.context.product.domain.model.Product;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductFacade {
	private final ProductReader productReader;
	private final ProductWriter productWriter;

	@Log
	public Product getProductDetail(Long productId) {
		return productReader.getProductDetailByProductId(productId);
	}

	@Log
	public List<Product> getProductList(ProductListDTO.Request request) {
		return productReader.getProductList(request);
	}

	@Log
	public Product postProduct(ProductPostDTO.Request request) {
		return productWriter.postProduct(request);
	}

}
