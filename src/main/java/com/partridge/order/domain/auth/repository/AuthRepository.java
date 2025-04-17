package com.partridge.order.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.partridge.order.global.entity.User;

public interface AuthRepository extends JpaRepository<User, Long> {
	boolean existsByEmailAndPassword(String email, String password);
}
