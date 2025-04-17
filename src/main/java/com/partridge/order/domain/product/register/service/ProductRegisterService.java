package com.partridge.order.domain.product.register.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.domain.product.dto.ProductRegisterDTO;
import com.partridge.order.domain.product.register.repository.ProductRegisterRepository;
import com.partridge.order.global.entity.Product;
import com.partridge.order.global.exception.businessExceptions.DatabaseCreationException;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductRegisterService {
	private final ProductRegisterRepository productRegisterRepository;

	@Transactional
	public ProductRegisterDTO.Response postProduct(ProductRegisterDTO.Request request) {
		try {
			return registerResponseBuilder(productRegisterRepository.save(request.toEntity()));
		} catch (DataIntegrityViolationException | PersistenceException e) {
			throw new DatabaseCreationException(e.getMessage());
		}
	}

	private ProductRegisterDTO.Response registerResponseBuilder(Product product) {
		return ProductRegisterDTO.Response.builder()
			.key(product.getKey())
			.build();
	}
}
