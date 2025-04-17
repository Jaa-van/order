package com.partridge.order.domain.product.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.partridge.order.global.entity.Product;

@Repository
public interface ProductRegisterRepository extends JpaRepository<Product, Long> {
}
