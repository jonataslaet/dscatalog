package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	private Long existingId;
	private Long nonExistingId;
	private Product productToBeSaved;
	private Long countedTotalProducts;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 9999999L;
		productToBeSaved = Factory.createProduct();
		countedTotalProducts = productRepository.count();
	}
	
	@Test
	void deleteShouldDeleteObjectWhenIdExists() {
		productRepository.deleteById(existingId);
		Optional<Product> productFound = productRepository.findById(existingId);
		Assertions.assertFalse(productFound.isPresent());
	}

	@Test
	void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		Optional<Product> productFound = productRepository.findById(nonExistingId);
		Assertions.assertFalse(productFound.isPresent());
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			productRepository.deleteById(nonExistingId);
		});
	}
	
	@Test
	void saveShouldSaveProductWhenIdDoesNotExist() {
		productToBeSaved.setId(null);
		Product save = productRepository.save(productToBeSaved);
		Assertions.assertNotNull(save.getId());
		Assertions.assertEquals(countedTotalProducts+1, save.getId());
	}
}
