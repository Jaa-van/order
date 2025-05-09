package com.partridge.order.context.order.controller.dto;

import static com.partridge.order.global.util.DeliveryUtil.*;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.domain.model.Order;

@Component
public class OrderResponseMapper {
	public OrderPostKeyDTO.Resposne toOrderPostKeyResponse(String key) {
		return OrderPostKeyDTO.Resposne.builder()
			.key(key)
			.build();
	}

	public OrderPostDTO.Response toOrderPostResponse(Order order) {
		return OrderPostDTO.Response.builder()
			.orderId(order.getId())
			.key(order.getKey())
			.totalPrice(order.getTotalPrice())
			.deliveryFee(caculateDeliveryFee(order.getDeliveryFee()))
			.status(order.getStatus())
			.build();
	}
}
