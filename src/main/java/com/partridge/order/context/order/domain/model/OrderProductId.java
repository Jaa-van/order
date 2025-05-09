package com.partridge.order.context.order.domain.model;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderProductId implements Serializable {
	private static final long serialVersionUID = 6263297829656228566L;
	@Column(name = "order_id", nullable = false)
	private Long orderId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		OrderProductId entity = (OrderProductId)o;
		return Objects.equals(this.productId, entity.productId) &&
			Objects.equals(this.orderId, entity.orderId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, orderId);
	}

}