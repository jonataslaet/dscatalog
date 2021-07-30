package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsuperior.dscatalog.entities.Product;

@DataJpaTest
class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	@Test
	void deleteShouldDeleteObjectWhenIdExists() {
		Long existingId = 1L;
		
		productRepository.deleteById(existingId);
		
		Optional<Product> productFound = productRepository.findById(existingId);
		
		Assertions.assertFalse(productFound.isPresent());
	}

}
