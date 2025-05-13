package com.partridge.order.context.order.service.validator;

import static com.partridge.order.context.order.constant.OrderCommonCodeEnum.*;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.constant.OrderCommonCodeEnum;
import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.exception.DuplicateOrderRequestException;
import com.partridge.order.context.order.exception.InvalidStatusException;
import com.partridge.order.context.order.exception.InventoryNotEnoughException;
import com.partridge.order.context.order.exception.OrderExpiredException;
import com.partridge.order.context.order.infra.OrderRedisUtil;
import com.partridge.order.context.product.service.dto.ProductDto;
import com.partridge.order.global.constant.ConstantValue;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderServiceValidator {
	private final OrderRedisUtil orderRedisUtil;

	public void validateOrderKey(String key) {
		String status = orderRedisUtil.getOrder(key);

		if (ORDER_IN_PROGRESS.getCode().equals(status) || ORDER_COMPLETE.getCode().equals(status)) {
			throw new DuplicateOrderRequestException();
		} else if (ORDER_EXPIRED.getCode().equals(status)) {
			throw new OrderExpiredException();
		} else {
			throw new InvalidStatusException(ConstantValue.ORDER_KR);
		}
	}

	public void validateProductInventory(OrderPostDto.Request request,
		Map<Long, ProductDto> productDtoMap) {
		request.getProducts().forEach(product -> {
			if (product.getQuantity() > productDtoMap.get(product.getProductId()).getInventory()) {
				throw new InventoryNotEnoughException(productDtoMap.get(product.getProductId()).getName());
			}
		});
	}
}
