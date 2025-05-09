package com.partridge.order.context.product.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.partridge.order.context.product.domain.model.Product;

@Component
public class ProductResponseMapper {

	public ProductDetailDTO.Response toProductDetailResponse(Product product) {
		return ProductDetailDTO.Response.builder()
			.productId(product.getId())
			.key(product.getKey())
			.name(product.getName())
			.inventory(product.getInventory())
			.price(product.getPrice())
			.description(product.getDescription())
			.build();
	}

	public ProductListDTO.Response toProductListResponse(List<Product> productList) {
		return ProductListDTO.Response.builder()
			.products(productList.stream().map(this::productListResponseBuilder).collect(Collectors.toList()))
			.totalCount(productList.size())
			.build();
	}

	private ProductListDTO.ResponseProduct productListResponseBuilder(Product product) {
		return ProductListDTO.ResponseProduct.builder()
			.productId(product.getId())
			.key(product.getKey())
			.name(product.getName())
			.inventory(product.getInventory())
			.price(product.getPrice())
			.description(product.getDescription())
			.build();
	}

	public ProductPostDTO.Response toProductPostResponse(Product product) {
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
