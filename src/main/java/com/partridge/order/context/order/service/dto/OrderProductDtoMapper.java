package com.partridge.order.context.order.service.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.controller.dto.OrderPostDTO;
import com.partridge.order.context.order.domain.model.OrderProduct;
import com.partridge.order.context.order.domain.model.OrderProductId;

@Component
public class OrderProductDtoMapper {
	public OrderProduct toEntity(Long orderId, OrderPostDTO.RequestProduct requestProducts) {
		return OrderProduct.builder()
			.id(OrderProductId.builder()
				.orderId(orderId)
				.productId(requestProducts.getProductId())
				.build())
			.quantity(requestProducts.getQuantity())
			.build();
	}
}
