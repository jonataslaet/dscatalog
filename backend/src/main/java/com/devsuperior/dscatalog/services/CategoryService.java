package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.resources.dtos.CategoryDTO;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly=true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		Page<Category> categoriesPaged = categoryRepository.findAll(pageable);
		return categoriesPaged.map(c -> new CategoryDTO(c));
	}
	
	@Transactional(readOnly=true)
	public CategoryDTO findById(Long id) {
		Optional<Category> categoryOptional = categoryRepository.findById(id);
		categoryOptional.orElseThrow(() -> new EntityNotFoundException("Category not found"));
		return new CategoryDTO(categoryOptional.get());
	}

	public CategoryDTO insert(CategoryDTO categoryDTO) {
		Category category = new Category(categoryDTO);
		category = categoryRepository.save(category);
		return new CategoryDTO(category);
	}

	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		try {
			findById(id);
			categoryDTO.setId(id);
			Category category = categoryRepository.save(new Category(categoryDTO));
			return new CategoryDTO(category);
		}
		catch (Exception e) {
			throw new EntityNotFoundException("asf");
		}
		
	}
	
	public void delete(Long id) {
		try {
			findById(id);
			categoryRepository.deleteById(id);
		}
		catch (Exception e) {
			throw new EntityNotFoundException("asf");
		}
		
	}

}
