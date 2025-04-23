package com.partridge.order.domain.order.validator;

import static com.partridge.order.domain.order.constant.OrderCommonCode.*;
import static com.partridge.order.domain.order.constant.OrderConstantValue.*;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.partridge.order.domain.order.dto.OrderPostDTO;
import com.partridge.order.domain.order.exception.DuplicateOrderRequestException;
import com.partridge.order.domain.order.exception.InventoryNotEnoughException;
import com.partridge.order.domain.order.exception.OrderExpiredException;
import com.partridge.order.domain.order.redis.OrderRedisUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidator {
	private final OrderRedisUtil orderRedisUtil;

	public void validateOrderKey(String key) {
		switch (orderRedisUtil.getOrder(key)) {
			case ORDER_IN_PROGRESS:
			case ORDER_COMPLETE:
				throw new DuplicateOrderRequestException();
			case ORDER_EXPIRED:
				throw new OrderExpiredException();
		}
	}

	public void validateProductInventory(OrderPostDTO.Request request,
		Map<Long, OrderPostDTO.ProductInformation> productInformationMap) {
		request.getProducts().forEach(product -> {
			if (product.getQuantity() > productInformationMap.get(product.getProductId()).getInventory()) {
				throw new InventoryNotEnoughException(productInformationMap.get(product.getProductId()).getName());
			}
			});
	}
}
