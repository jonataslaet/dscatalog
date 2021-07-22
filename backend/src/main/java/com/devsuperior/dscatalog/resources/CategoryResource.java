package com.devsuperior.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	
	@PostMapping
	ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO) {
		CategoryDTO createdCategoryDTO = categoryService.insert(categoryDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCategoryDTO.getId())
				.toUri();
		return ResponseEntity.created(uri).body(createdCategoryDTO);
	}
	
	@PutMapping("/{id}")
	ResponseEntity<CategoryDTO> update(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO categoryDTO2 = categoryService.update(id, categoryDTO);
		return ResponseEntity.status(204).body(categoryDTO2);
	}
}
