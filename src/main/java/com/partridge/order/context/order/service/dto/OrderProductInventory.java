package com.partridge.order.context.order.service.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = {"orderId", "productId", "quantity", "inventory"})
public class OrderProductInventory {
	private final Long orderId;
	private final Long productId;
	private final Long quantity;
	private final String name;
	private final Long inventory;
}
