package com.partridge.order.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.partridge.order.global.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
