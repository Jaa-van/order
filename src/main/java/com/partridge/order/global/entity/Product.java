package com.partridge.order.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "category")
	private Long category;

	@Column(name = "detail_category")
	private Long detailCategory;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "price")
	private Long price;

	@Column(name = "`explain`", length = 4000)
	private String explain;

}