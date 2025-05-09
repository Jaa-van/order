package com.partridge.order.context.order.domain.validator;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.exception.InventoryNotEnoughException;
import com.partridge.order.context.product.service.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidator {

	public void validateProductInventory(OrderPostDto.Request request,
		Map<Long, ProductDto> productDtoMap) {
		request.getProducts().forEach(product -> {
			if (product.getQuantity() > productDtoMap.get(product.getProductId()).getInventory()) {
				throw new InventoryNotEnoughException(productDtoMap.get(product.getProductId()).getName());
			}
			});
	}
}
