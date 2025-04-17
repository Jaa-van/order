package com.partridge.order.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`order`")
public class Order {
	@Id
	@Column(name = "`order`", nullable = false)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "`key`", nullable = false, length = 100)
	private String key;

	@Column(name = "total_price")
	private Long totalPrice;

	@Column(name = "delivery_fee")
	private Boolean deliveryFee;

	@Column(name = "status", length = 10)
	private String status;

}