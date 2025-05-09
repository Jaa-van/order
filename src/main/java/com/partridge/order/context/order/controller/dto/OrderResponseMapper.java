package com.partridge.order.context.order.controller.dto;

import static com.partridge.order.global.util.DeliveryUtil.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.global.util.DeliveryUtil;

@Component
public class OrderResponseMapper {
	public OrderPostKeyDto.Resposne toOrderPostKeyResponse(String key) {
		return OrderPostKeyDto.Resposne.builder()
			.key(key)
			.build();
	}

	public OrderPostDto.Response toOrderPostResponse(Order order) {
		return OrderPostDto.Response.builder()
			.orderId(order.getId())
			.key(order.getKey())
			.totalPrice(order.getTotalPrice())
			.deliveryFee(caculateDeliveryFee(order.getDeliveryFee()))
			.status(order.getStatus())
			.build();
	}

	public OrderListDto.Response toOrderListResponse(List<Order> orders) {
		return OrderListDto.Response.builder()
			.orders(orderListResponseOrderBuilder(orders))
			.totalCount((long)orders.size())
			.build();
	}

	private List<OrderListDto.ResponseOrder> orderListResponseOrderBuilder(List<Order> orderList) {
		return orderList.stream().map(order -> OrderListDto.ResponseOrder.builder()
				.orderId(order.getId())
				.key(order.getKey())
				.totalPrice(order.getTotalPrice())
				.deliveryFee(DeliveryUtil.caculateDeliveryFee(order.getDeliveryFee()))
				.status(order.getStatus())
				.build())
			.toList();
	}
}
