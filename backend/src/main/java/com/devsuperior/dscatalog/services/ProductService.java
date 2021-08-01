package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.resources.dtos.CategoryDTO;
import com.devsuperior.dscatalog.resources.dtos.ProductDTO;
import com.devsuperior.dscatalog.resources.exceptions.DatabaseException;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly=true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> productsPaged = productRepository.findAll(pageable);
		return productsPaged.map(c -> new ProductDTO(c));
	}
	
	@Transactional(readOnly=true)
	public ProductDTO findById(Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		productOptional.orElseThrow(() -> new EntityNotFoundException("Product not found"));
		return new ProductDTO(productOptional.get(), productOptional.get().getCategories());
	}

	public ProductDTO insert(ProductDTO productDTO) {
		Product product = new Product(productDTO);
		product = productRepository.save(product);
		return new ProductDTO(product);
	}

	public ProductDTO update(Long id, ProductDTO productDTO) {
		Product product = new Product();
		try {
			findById(id);
			copyDtoToEntity(productDTO, product);
			product.setId(id);
			productRepository.save(product);
			return new ProductDTO(product);
		}
		catch (Exception e) {
			throw new EntityNotFoundException("Product not found");
		}
		
	}
	
	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Product not found");
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Product depends on other entity");
		}
		
	}
	
	private void copyDtoToEntity(ProductDTO productDTO, Product product) {
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setImgUrl(productDTO.getImgUrl());
		product.setPrice(productDTO.getPrice());
		product.setDate(productDTO.getDate());
		
		product.getCategories().clear();
		
		for (CategoryDTO catDTO : productDTO.getCategories()) {
			Category cat = categoryRepository.findById(catDTO.getId()).get();
			product.getCategories().add(cat);
		}
	}

}
