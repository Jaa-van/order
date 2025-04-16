package com.partridge.order.domain.product.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.partridge.order.global.entity.Product;

@Repository
public interface ProductSearchRepository extends JpaRepository<Product, Long> {
}
