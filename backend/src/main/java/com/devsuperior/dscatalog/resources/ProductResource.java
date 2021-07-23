package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.resources.dtos.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

	@Autowired
	private ProductService productService;
	
	@GetMapping
	ResponseEntity<Page<ProductDTO>> findAllPaged(
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "5") Integer linesPerPage, 
			@RequestParam(value="direction", defaultValue = "ASC") String direction, 
			@RequestParam(value="orderBy", defaultValue = "name") String orderBy
			) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<ProductDTO> productsDTO = productService.findAllPaged(pageRequest);

		return ResponseEntity.ok().body(productsDTO);
	}
	
	@GetMapping("/{id}")
	ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id) {
		
		ProductDTO productDTO = productService.findById(id);

		return ResponseEntity.ok().body(productDTO);
	}
	
	@PostMapping
	ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO) {
		ProductDTO createdProductDTO = productService.insert(productDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdProductDTO.getId())
				.toUri();
		return ResponseEntity.created(uri).body(createdProductDTO);
	}
	
	@PutMapping("/{id}")
	ResponseEntity<ProductDTO> update(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
		ProductDTO productDTO2 = productService.update(id, productDTO);
		return ResponseEntity.status(204).body(productDTO2);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
