package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	ResponseEntity<Page<CategoryDTO>> findAllPaged(Pageable pageable) {
		Page<CategoryDTO> categoriesDTO = categoryService.findAllPaged(pageable);

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
	
	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
