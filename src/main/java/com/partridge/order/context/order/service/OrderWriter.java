package com.partridge.order.context.order.service;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.context.order.controller.dto.OrderPostDTO;
import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.order.repository.OrderProductRepotisory;
import com.partridge.order.context.order.repository.OrderRepository;
import com.partridge.order.context.order.service.dto.OrderDtoMapper;
import com.partridge.order.context.order.service.dto.OrderProductDtoMapper;
import com.partridge.order.context.product.service.dto.ProductDto;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderWriter {
	private final OrderRepository orderRepository;
	private final OrderProductRepotisory orderProductRepotisory;
	private final OrderDtoMapper orderDtoMapper;
	private final OrderProductDtoMapper orderProductDtoMapper;

	@Log
	@Transactional
	public Order postOrder(OrderPostDTO.Request request, Map<Long, ProductDto> productDtoMap) {
		Order order = orderRepository.save(orderDtoMapper.toEntity(request, productDtoMap));
		request.getProducts().stream()
			.map(product -> orderProductDtoMapper.toEntity(order.getId(), product))
			.forEach(orderProductRepotisory::save);

		return order;
	}
}
