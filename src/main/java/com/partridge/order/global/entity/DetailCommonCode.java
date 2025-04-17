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
@Table(name = "detail_common_code")
public class DetailCommonCode {
	@EmbeddedId
	private DetailCommonCodeId id;

	@Column(name = "value", length = 100)
	private String value;

}