package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;

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

	@Test
	void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
		Long nonExistingId = 9999999L;
		
		Optional<Product> productFound = productRepository.findById(nonExistingId);
		
		Assertions.assertFalse(productFound.isPresent());
		
		Assertions.assertThrows(EntityNotFoundException.class, ()->{
			productRepository.deleteById(nonExistingId);
		});
	}
}
