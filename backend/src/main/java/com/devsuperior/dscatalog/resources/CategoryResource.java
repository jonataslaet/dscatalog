package com.devsuperior.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@GetMapping
	ResponseEntity<List<Category>> findAll() {
		
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(1L, "Clothes"));
		categories.add(new Category(2L, "Books"));
		categories.add(new Category(3L, "Eletronics"));

		return ResponseEntity.ok().body(categories);
	}
}
