package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.resources.dtos.ProductDTO;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Transactional(readOnly=true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> productsPaged = productRepository.findAll(pageRequest);
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
		try {
			findById(id);
			productDTO.setId(id);
			Product product = productRepository.save(new Product(productDTO));
			return new ProductDTO(product);
		}
		catch (Exception e) {
			throw new EntityNotFoundException("asf");
		}
		
	}
	
	public void delete(Long id) {
		try {
			findById(id);
			productRepository.deleteById(id);
		}
		catch (Exception e) {
			throw new EntityNotFoundException("asf");
		}
		
	}

}
