package com.partridge.order.context.order.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderReader {
	private final OrderRepository orderRepository;

	public List<Order> getOrderListByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}
}
