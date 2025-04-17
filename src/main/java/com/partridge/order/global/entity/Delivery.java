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
@Table(name = "delivery")
public class Delivery {
	@Id
	@Column(name = "delivery_id", nullable = false)
	private Long id;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "address", length = 4000)
	private String address;

	@Column(name = "status", length = 10)
	private String status;

}