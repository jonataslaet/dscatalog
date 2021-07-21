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
}
