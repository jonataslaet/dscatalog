package com.devsuperior.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query("SELECT DISTINCT prod FROM Product prod INNER JOIN prod.categories cats "
			+ "WHERE (:category IS NULL OR :category IN cats) "
			+ "AND (LOWER(prod.name) LIKE LOWER(CONCAT('%',:prodName,'%')))")
	Page<Product> find(Category category, String prodName, Pageable pageable);

}
