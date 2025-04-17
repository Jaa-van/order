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
@Table(name = "payment")
public class Payment {
	@Id
	@Column(name = "payment_id", nullable = false)
	private Long id;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "method", length = 10)
	private String method;

	@Column(name = "status", length = 10)
	private String status;

}