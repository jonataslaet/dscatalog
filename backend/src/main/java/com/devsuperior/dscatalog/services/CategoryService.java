package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<CategoryDTO> findAll() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(c -> new CategoryDTO(c)).collect(Collectors.toList());
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
