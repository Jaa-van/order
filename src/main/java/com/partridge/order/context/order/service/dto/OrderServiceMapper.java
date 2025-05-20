package com.partridge.order.context.order.service.dto;

import static com.partridge.order.context.order.constant.OrderCommonCodeEnum.*;
import static com.partridge.order.global.util.DeliveryUtil.*;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.product.service.dto.ProductDto;

@Component
public class OrderServiceMapper {
	public Order toEntity(OrderPostDto.Request request, Map<Long, ProductDto> productDtoMap) {
		Long totalPrice = request.getProducts().stream()
			.mapToLong(product -> productDtoMap.get(product.getProductId()).getPrice() * product.getQuantity())
			.sum();
		return Order.builder()
			.key(request.getKey())
			.userId(request.getUserId())
			.totalPrice(totalPrice)
			.deliveryFee(deliveryFeeYn(totalPrice))
			.status(ORDER_IN_PROGRESS.getCode())
			.build();
	}
}
