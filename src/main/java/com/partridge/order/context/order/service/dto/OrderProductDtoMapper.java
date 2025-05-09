package com.partridge.order.context.order.service.dto;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.domain.model.OrderProduct;
import com.partridge.order.context.order.domain.model.OrderProductId;

@Component
public class OrderProductDtoMapper {
	public OrderProduct toEntity(Long orderId, OrderPostDto.RequestProduct requestProducts) {
		return OrderProduct.builder()
			.id(OrderProductId.builder()
				.orderId(orderId)
				.productId(requestProducts.getProductId())
				.build())
			.quantity(requestProducts.getQuantity())
			.build();
	}
}
