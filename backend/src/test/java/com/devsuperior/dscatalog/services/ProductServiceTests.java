package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.resources.dtos.ProductDTO;
import com.devsuperior.dscatalog.resources.exceptions.DatabaseException;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
class ProductServiceTests {

	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long existingId;
	
	private Long nonExistingId;
	
	private Long dependingId;
	
	private PageImpl<Product> page;
	
	private Product product;
	
	private Category category;
	
	@BeforeEach
	private void setUp() {
		existingId = 1L;
		nonExistingId = 2L;
		dependingId = 3L;
		product = Factory.createProduct();
		category = product.getCategories().stream().findFirst().get();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(categoryRepository.findById(existingId)).thenReturn(Optional.of(category));
		
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependingId);
	}
	
	@Test
	void updateShouldUpdateProductWhenIdExists() {
		ProductDTO expectedProductDTO = new ProductDTO(product, product.getCategories());
		expectedProductDTO.setDescription("Atualizando");
		
		ProductDTO productDTO = productService.update(existingId, expectedProductDTO);
		Assertions.assertNotNull(productDTO);
		Assertions.assertEquals(expectedProductDTO, productDTO);
		
		Mockito.verify(productRepository, Mockito.times(1)).findById(existingId);
		Mockito.verify(categoryRepository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EntityNotFoundException.class, ()->{
			productService.findById(nonExistingId);
		});
		
		Mockito.verify(productRepository, times(1)).findById(nonExistingId);
	}
	
	@Test
	void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO expectedProductDTO = new ProductDTO(product);
		
		ProductDTO productDTO = productService.findById(existingId);
		Assertions.assertNotNull(productDTO);
		Assertions.assertEquals(expectedProductDTO, productDTO);
		
		Mockito.verify(productRepository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = productService.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		
		Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			productService.delete(existingId);
		});
		
		Mockito.verify(productRepository, times(1)).deleteById(existingId);
	}

	@Test
	void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EntityNotFoundException.class, ()->{
			productService.delete(nonExistingId);
		});
		
		Mockito.verify(productRepository, times(1)).deleteById(nonExistingId);
	}
	
	@Test
	void deleteShouldThrowDatabaseExceptionWhenIdExists() {
		
		Assertions.assertThrows(DatabaseException.class, ()->{
			productService.delete(dependingId);
		});
		
		Mockito.verify(productRepository, times(1)).deleteById(dependingId);
	}

}
