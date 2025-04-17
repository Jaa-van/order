package com.partridge.order.global.entity;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DetailCommonCodeId implements Serializable {
	private static final long serialVersionUID = 4311920945276784789L;
	@Column(name = "code", nullable = false, length = 10)
	private String code;

	@Column(name = "detail_code", nullable = false, length = 10)
	private String detailCode;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		DetailCommonCodeId entity = (DetailCommonCodeId)o;
		return Objects.equals(this.code, entity.code) &&
			Objects.equals(this.detailCode, entity.detailCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, detailCode);
	}

}