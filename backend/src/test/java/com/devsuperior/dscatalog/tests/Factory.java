package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.resources.dtos.ProductDTO;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", 800.0, Instant.parse("2020-07-20T03:00:00Z"), "Good Phone", "http://img.com/img.png");
		product.getCategories().add(new Category(1L, "Eletronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
}
