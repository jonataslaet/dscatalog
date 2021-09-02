package com.devsuperior.dscatalog.repositories;

import java.util.List;

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
			+ "WHERE (COALESCE(:categories) IS NULL OR cats IN :categories) "
			+ "AND (LOWER(prod.name) LIKE LOWER(CONCAT('%',:prodName,'%')))")
	Page<Product> find(List<Category> categories, String prodName, Pageable pageable);

}
