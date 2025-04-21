package com.partridge.order.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.domain.cart.dto.CartListDTO;
import com.partridge.order.global.entity.Cart;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO cart (user_id, product_id, quantity) " +
		"VALUES (:userId, :productId, :quantity) " +
		"ON DUPLICATE KEY UPDATE quantity = :quantity",
		nativeQuery = true)
	void upsertCart(@Param("productId") Long userId,
		@Param("userId") Long productId,
		@Param("quantity") Long quantity);
}
