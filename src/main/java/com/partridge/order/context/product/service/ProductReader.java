package com.partridge.order.context.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.partridge.order.context.product.controller.dto.ProductListDTO;
import com.partridge.order.context.product.domain.model.Product;
import com.partridge.order.context.product.repository.ProductRepository;
import com.partridge.order.context.product.service.dto.ProductDto;
import com.partridge.order.context.product.service.dto.ProductDtoMapper;
import com.partridge.order.global.constant.ConstantValue;
import com.partridge.order.global.exception.global.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductReader {
	private final ProductRepository productRepository;
	private final ProductDtoMapper productDtoMapper;

	public Map<Long, ProductDto> getProductDtoMapByProductIdList(List<Long> productIdList) {
		return productDtoMapper.toProductDtoMap(productRepository.findAllById(productIdList));
	}

	public Product getProductDetailByProductId(Long productId) {
		return productRepository.findById(productId).orElseThrow(() -> new NotFoundException(ConstantValue.PRODUCT_KR));
	}

	public List<Product> getProductList(ProductListDTO.Request request) {
		return productRepository.getProductsByKeywordAndSortByOrder(request);
	}
}
