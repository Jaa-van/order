package com.partridge.order.domain.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.partridge.order.domain.product.dto.ProductDetailDTO;
import com.partridge.order.domain.product.dto.ProductListDTO;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.constant.ConstantValue;
import com.partridge.order.global.entity.Product;
import com.partridge.order.global.exception.businessExceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public ProductDetailDTO.Response getProduct(Long productId) {
		return productRepository.findById(productId)
			.map(this::productResponseBuilder)
			.orElseThrow(() -> new NotFoundException(ConstantValue.PRODUCT));
	}

	public ProductListDTO.Response getProducts(ProductListDTO.Request request) {
		return productListResponseBuilder(productRepository.getProductsByKeywordAndSortByOrder(request));
	}

	private ProductDetailDTO.Response productResponseBuilder(Product product) {
		return ProductDetailDTO.Response.builder()
			.productId(product.getId())
			.key(product.getKey())
			.name(product.getName())
			.quantity(product.getQuantity())
			.price(product.getPrice())
			.description(product.getDescription())
			.build();
	}

	private ProductListDTO.Response productListResponseBuilder(List<ProductListDTO.ResponseProduct> responseProducts) {
		return ProductListDTO.Response.builder()
			.products(responseProducts)
			.totalCount(responseProducts.size())
			.build();
	}
}
