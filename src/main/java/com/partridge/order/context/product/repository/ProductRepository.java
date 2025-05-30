package com.partridge.order.context.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.partridge.order.context.product.domain.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

	@Modifying
	@Query(value = "update Product p "
		+ "set p.inventory = p.inventory - :quantity "
		+ "where p.id = :productId")
	void updateProductInventory(Long productId, Long quantity);
}
