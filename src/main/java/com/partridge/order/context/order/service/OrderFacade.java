package com.partridge.order.context.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.context.order.infra.OrderRedisUtil;
import com.partridge.order.context.order.service.validator.OrderServiceValidator;
import com.partridge.order.context.product.service.ProductReader;
import com.partridge.order.context.product.service.dto.ProductDto;
import com.partridge.order.global.util.KeyUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderFacade {
	private final OrderRedisUtil orderRedisUtil;
	private final OrderServiceValidator orderServiceValidator;
	private final ProductReader productReader;
	private final OrderWriter orderWriter;
	private final OrderReader orderReader;

	public String postOrderKey() {
		String key = KeyUtil.generateKey();
		orderRedisUtil.setOrderWaiting(key);

		return key;
	}

	public Order postOrder(OrderPostDto.Request request) {
		orderServiceValidator.validateOrderKey(request.getKey());
		Map<Long, ProductDto> productDtoMap = productReader.getProductDtoMapByProductIdList(
			request.getProducts().stream().map(OrderPostDto.RequestProduct::getProductId).toList());
		orderServiceValidator.validateProductInventory(request, productDtoMap);

		Order order = orderWriter.postOrder(request, productDtoMap);

		orderRedisUtil.setOrderProgress(request.getKey());

		return order;
	}

	public List<Order> getOrderList(Long userId) {
		return orderReader.getOrderListByUserId(userId);
	}
}
