package com.partridge.order.context.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.partridge.order.context.order.service.dto.OrderProductInventory;
import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;
import com.partridge.order.context.order.domain.model.OrderProduct;

@Repository
public interface OrderProductRepotisory extends JpaRepository<OrderProduct, Long> {
	List<OrderProduct> findByIdOrderId(Long orderId);

	@Query(value = "select op.order_id, op.product_id, op.quantity, p.name, p.inventory"
		+ " from order_product op"
		+ " inner join product p on op.product_id = p.product_id"
		+ " where op.order_id = :orderId "
		+ "FOR UPDATE"
		, nativeQuery = true)
	List<OrderProductInventory> getInventoryByOrderId(Long orderId);
}
