package com.devsuperior.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.resources.dtos.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

	private Long existingId;
	private Long nonExistingId;
	private Long countProducts;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TokenUtil tokenUtil;

	private String username;

	private String password;

	@BeforeEach
	private void setUp() {
		existingId = 1L;
		nonExistingId = 999999999L;
		countProducts = 25L;
		username = "maria@gmail.com";
		password = "123456";
	}

	@Test
	void findAllShouldReturnSortedPage0Size3WhenSortedByNameDesc() throws Exception {
		ResultActions request = mockMvc
				.perform(get("/products?page=0&size=3&sort=name,desc").accept(MediaType.APPLICATION_JSON));

		request.andExpect(status().isOk());
		request.andExpect(jsonPath("$.totalElements").value(countProducts));
		request.andExpect(jsonPath("$.content").exists());
		request.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		request.andExpect(jsonPath("$.content[1].name").value("Smart TV"));
		request.andExpect(jsonPath("$.content[2].name").value("Rails for Dummies"));
	}

	@Test
	void findAllShouldReturnSortedPage1Size3WhenSortedByNameDesc() throws Exception {
		ResultActions request = mockMvc
				.perform(get("/products?page=1&size=3&sort=name,desc").accept(MediaType.APPLICATION_JSON));

		request.andExpect(status().isOk());
		request.andExpect(jsonPath("$.totalElements").value(countProducts));
		request.andExpect(jsonPath("$.content").exists());
		request.andExpect(jsonPath("$.content[0].name").value("PC Gamer Y"));
		request.andExpect(jsonPath("$.content[1].name").value("PC Gamer X"));
		request.andExpect(jsonPath("$.content[2].name").value("PC Gamer Weed"));
	}

	@Test
	void findAllShouldReturnSortedPage2Size3WhenSortedByNameDesc() throws Exception {
		ResultActions request = mockMvc
				.perform(get("/products?page=2&size=3&sort=name,desc").accept(MediaType.APPLICATION_JSON));

		request.andExpect(status().isOk());
		request.andExpect(jsonPath("$.totalElements").value(countProducts));
		request.andExpect(jsonPath("$.content").exists());
		request.andExpect(jsonPath("$.content[0].name").value("PC Gamer Tx"));
		request.andExpect(jsonPath("$.content[1].name").value("PC Gamer Turbo"));
		request.andExpect(jsonPath("$.content[2].name").value("PC Gamer Tr"));
	}

	@Test
	void findAllShouldReturnSortedPage0Size3WhenSortedByNameAsc() throws Exception {
		ResultActions request = mockMvc
				.perform(get("/products?page=0&size=3&sort=name,asc").accept(MediaType.APPLICATION_JSON));

		request.andExpect(status().isOk());
		request.andExpect(jsonPath("$.totalElements").value(countProducts));
		request.andExpect(jsonPath("$.content").exists());
		request.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		request.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		request.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}

	@Test
	void updateProductShouldProductDTOWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ProductDTO productDTO = Factory.createProductDTO();
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions request = mockMvc
				.perform(put("/products/{id}", existingId).header("Authorization", "Bearer " + accessToken)
						.content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		Assertions.assertEquals(existingId, productDTO.getId());
		request.andExpect(status().isNoContent());
		request.andExpect(jsonPath("$.id").value(productDTO.getId()));
		request.andExpect(jsonPath("$.name").value(productDTO.getName()));
	}

	@Test
	void updateProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

		ProductDTO productDTO = Factory.createProductDTO();
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions request = mockMvc
				.perform(put("/products/{id}", nonExistingId).header("Authorization", "Bearer " + accessToken)
						.content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		Assertions.assertNotEquals(nonExistingId, productDTO.getId());
		request.andExpect(status().isNotFound());
	}

}
