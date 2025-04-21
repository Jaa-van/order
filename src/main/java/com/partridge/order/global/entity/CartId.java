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
public class CartId implements Serializable {
	private static final long serialVersionUID = -2200256287477129297L;
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		CartId entity = (CartId)o;
		return Objects.equals(this.productId, entity.productId) &&
			Objects.equals(this.userId, entity.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, userId);
	}

}