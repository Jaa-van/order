package com.partridge.order.domain.mypage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.partridge.order.global.entity.Order;

@Repository
public interface MypageRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);
}
