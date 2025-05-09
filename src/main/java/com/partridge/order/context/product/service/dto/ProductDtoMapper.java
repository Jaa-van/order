package com.partridge.order.context.product.service.dto;

import static com.partridge.order.global.util.KeyUtil.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.partridge.order.context.product.controller.dto.ProductPostDTO;
import com.partridge.order.context.product.domain.model.Product;

@Component
public class ProductDtoMapper {
	public Map<Long, ProductDto> toProductDtoMap(List<Product> products) {
		return products.stream().collect(Collectors.toMap(Product::getId, this::productDtoBuilder));
	}

	public Product toEntity(ProductPostDTO.Request request, String key) {
		return Product.builder()
			.key(key)
			.name(request.getName())
			.inventory(request.getInventory())
			.price(request.getPrice())
			.description(request.getDescription())
			.build();
	}

	private ProductDto productDtoBuilder(Product product) {
		return ProductDto.builder()
			.productId(product.getId())
			.name(product.getName())
			.price(product.getPrice())
			.description(product.getDescription())
			.inventory(product.getInventory())
			.key(product.getKey())
			.build();
	}
}
