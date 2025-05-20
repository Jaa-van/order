package com.partridge.order.context.order.service;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.order.repository.OrderProductRepotisory;
import com.partridge.order.context.order.repository.OrderRepository;
import com.partridge.order.context.order.service.dto.OrderServiceMapper;
import com.partridge.order.context.order.service.dto.OrderProductDtoMapper;
import com.partridge.order.context.product.service.dto.ProductDto;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderWriter {
	private final OrderRepository orderRepository;
	private final OrderProductRepotisory orderProductRepotisory;
	private final OrderServiceMapper orderServiceMapper;
	private final OrderProductDtoMapper orderProductDtoMapper;

	public Order postOrder(OrderPostDto.Request request, Map<Long, ProductDto> productDtoMap) {
		Order order = orderRepository.save(orderServiceMapper.toEntity(request, productDtoMap));
		request.getProducts().stream()
			.map(product -> orderProductDtoMapper.toEntity(order.getId(), product))
			.forEach(orderProductRepotisory::save);

		return order;
	}

	public void updateOrderStatusByOrderId(Long orderId, String status) {
		orderRepository.updateStatusByOrderId(orderId, status);
	}
}
