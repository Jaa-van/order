package com.partridge.order.domain.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.partridge.order.domain.payment.dto.PaymentPostDTO;
import com.partridge.order.global.entity.OrderProduct;

@Repository
public interface OrderProductRepotisory extends JpaRepository<OrderProduct, Long> {
	List<OrderProduct> findByIdOrderId(Long orderId);

	@Query(value = "select op.order_id, op.product_id, op.quantity, p.name, p.inventory"
		+ " from order_product op"
		+ " inner join product p on op.product_id = p.product_id"
		+ " where op.order_id = :orderId "
		+ "FOR UPDATE", nativeQuery = true)
	List<PaymentPostDTO.OrderProductInventory> getInventoryByOrderId(Long orderId);
}
