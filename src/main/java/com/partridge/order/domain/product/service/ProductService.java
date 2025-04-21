package com.partridge.order.domain.product.service;

import static com.partridge.order.global.util.KeyUtil.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.partridge.order.domain.product.dto.ProductDTO;
import com.partridge.order.domain.product.dto.ProductListDTO;
import com.partridge.order.domain.product.dto.ProductPostDTO;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.constant.ConstantValue;
import com.partridge.order.global.entity.Product;
import com.partridge.order.global.exception.businessExceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public ProductDTO.Response getProduct(Long productId) {
		return productRepository.findById(productId)
			.map(this::productResponseBuilder)
			.orElseThrow(() -> new NotFoundException(ConstantValue.PRODUCT));
	}

	public ProductListDTO.Response getProductList(ProductListDTO.Request request) {
		return productListResponseBuilder(productRepository.getProductsByKeywordAndSortByOrder(request));
	}

	public ProductPostDTO.Response postProduct(ProductPostDTO.Request request) {
		return productRepository.findById(productRepository.save(postRequestToEntity(request)).getId())
			.map(this::productRegisterResponseBuilder)
			.orElseThrow(() -> new NotFoundException(ConstantValue.PRODUCT));
	}

	private Product postRequestToEntity(ProductPostDTO.Request request) {
		return Product.builder()
			.key(generateKey())
			.name(request.getName())
			.inventory(request.getInventory())
			.price(request.getPrice())
			.description(request.getDescription())
			.build();
	}

	private ProductDTO.Response productResponseBuilder(Product product) {
		return ProductDTO.Response.builder()
			.productId(product.getId())
			.key(product.getKey())
			.name(product.getName())
			.inventory(product.getInventory())
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

	private ProductPostDTO.Response productRegisterResponseBuilder(Product product) {
		return ProductPostDTO.Response.builder()
			.productId(product.getId())
			.key(product.getKey())
			.name(product.getName())
			.inventory(product.getInventory())
			.price(product.getPrice())
			.description(product.getDescription())
			.build();
	}
}
