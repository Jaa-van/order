package com.partridge.order.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_product")
public class OrderProduct {
	@EmbeddedId
	private OrderProductId id;

	@Column(name = "quantity")
	private Long quantity;

}