package com.partridge.order.context.order.service;

import static com.partridge.order.global.constant.ConstantValue.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.order.repository.OrderProductRepotisory;
import com.partridge.order.context.order.repository.OrderRepository;
import com.partridge.order.context.order.service.dto.OrderProductInventory;
import com.partridge.order.global.exception.global.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderReader {
	private final OrderRepository orderRepository;
	private final OrderProductRepotisory orderProductRepotisory;

	public List<Order> getOrderListByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	public List<OrderProductInventory> getProductInventoryListByOrderId(Long orderId) {
		return orderProductRepotisory.getInventoryByOrderId(orderId);
	}

	public Long getTotalPriceByOrderId(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException(ORDER_KR))
			.getTotalPrice();
	}
}
