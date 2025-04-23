package com.partridge.order.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.partridge.order.global.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Modifying
	@Query("update Order o "
		+ "set o.status = :status "
		+ "where o.id = :orderId")
	void updateStatusByOrderId(Long orderId, String status);
}
