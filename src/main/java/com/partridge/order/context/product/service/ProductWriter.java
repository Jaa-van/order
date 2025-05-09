package com.partridge.order.context.product.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.context.product.controller.dto.ProductPostDTO;
import com.partridge.order.context.product.domain.model.Product;
import com.partridge.order.context.product.repository.ProductRepository;
import com.partridge.order.context.product.service.dto.ProductDtoMapper;
import com.partridge.order.global.util.KeyUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductWriter {
	private final ProductRepository productRepository;
	private final ProductDtoMapper productDtoMapper;

	@Transactional
	public Product postProduct(ProductPostDTO.Request request) {
		return productRepository.save(productDtoMapper.toEntity(request, KeyUtil.generateKey()));
	}
}
