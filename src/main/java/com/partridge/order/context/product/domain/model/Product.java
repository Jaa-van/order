package com.partridge.order.context.product.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
	@Id
	@Column(name = "product_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "`key`", nullable = false, length = 100)
	private String key;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "inventory")
	private Long inventory;

	@Column(name = "price")
	private Long price;

	@Column(name = "description", length = 4000)
	private String description;
}