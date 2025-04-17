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
@Table(name = "common_code")
public class CommonCode {
	@Id
	@Column(name = "code", nullable = false, length = 10)
	private String code;

	@Column(name = "value", length = 100)
	private String value;

	@Column(name = "`column`", length = 100)
	private String column;

}