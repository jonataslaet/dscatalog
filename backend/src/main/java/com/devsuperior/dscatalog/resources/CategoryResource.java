package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.resources.dtos.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	ResponseEntity<List<CategoryDTO>> findAll() {
		
		List<CategoryDTO> categoriesDTO = categoryService.findAll();

		return ResponseEntity.ok().body(categoriesDTO);
	}
	
	@GetMapping("/{id}")
	ResponseEntity<CategoryDTO> findById(@PathVariable("id") Long id) {
		
		CategoryDTO categoryDTO = categoryService.findById(id);

		return ResponseEntity.ok().body(categoryDTO);
	}
}
