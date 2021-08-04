package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.resources.dtos.ProductDTO;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	
	private Long existingId;
	private Long nonExistingId;
	private Long countProducts;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@BeforeEach
	private void setUp() {
		existingId = 1L;
		nonExistingId = 999999999L;
		countProducts = 25L;
	}
	
	@Test
	void deleteShouldDeleteProductWhenIdExists() {
		productService.delete(existingId);
		Assertions.assertEquals(countProducts - 1, productRepository.count());
	}
	
	@Test
	void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EntityNotFoundException.class, ()->{
			productService.delete(nonExistingId);
		});
	}
	
	@Test
	void findAllPagedShouldReturnProductDTOPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> pageWithProductDTO = productService.findAllPaged(pageRequest);
		
		Assertions.assertFalse(pageWithProductDTO.isEmpty());
		Assertions.assertEquals(0, pageWithProductDTO.getNumber());
		Assertions.assertEquals(10, pageWithProductDTO.getSize());
		Assertions.assertEquals(countProducts, pageWithProductDTO.getTotalElements());
	}
	
	@Test
	void findAllPagedShouldReturnEmptyWhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(9999999, 10);
		Page<ProductDTO> pageWithProductDTO = productService.findAllPaged(pageRequest);
		
		Assertions.assertTrue(pageWithProductDTO.isEmpty());
		Assertions.assertEquals(countProducts, pageWithProductDTO.getTotalElements());
	}
	
	@Test
	void findAllPagedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("name"));
		Page<ProductDTO> pageWithProductDTO = productService.findAllPaged(pageRequest);
		
		Assertions.assertFalse(pageWithProductDTO.isEmpty());
		Assertions.assertEquals("Macbook Pro", pageWithProductDTO.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", pageWithProductDTO.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", pageWithProductDTO.getContent().get(2).getName());
	}
}
