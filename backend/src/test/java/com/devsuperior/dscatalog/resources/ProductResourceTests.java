package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.resources.dtos.ProductDTO;
import com.devsuperior.dscatalog.resources.exceptions.DatabaseException;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProductResourceTests {

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	private PageImpl<ProductDTO> page;

	private ProductDTO productDTO;

	private Long existingId;

	private Long nonExistingId;

	private Long dependingId;

	private String username;

	private String password;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		existingId = 1L;
		nonExistingId = 2L;
		dependingId = 3L;
		username = "maria@gmail.com";
		password = "123456";

		Mockito.when(productService.findAllPaged(any(), any(), any())).thenReturn(page);
		Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
		Mockito.when(productService.findById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(productService.update(existingId, productDTO)).thenReturn(productDTO);
		Mockito.when(productService.update(nonExistingId, productDTO)).thenThrow(EntityNotFoundException.class);

		doNothing().when(productService).delete(existingId);
		doThrow(EntityNotFoundException.class).when(productService).delete(nonExistingId);
		doThrow(DatabaseException.class).when(productService).delete(dependingId);
	}

	@Test
	void deleteShouldThrowDatabaseExceptionWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

		ResultActions request = mockMvc
				.perform(delete("/products/{id}", dependingId).header("Authorization", "Bearer " + accessToken));
		request.andExpect(status().isBadRequest());
	}

	@Test
	void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

		ResultActions request = mockMvc
				.perform(delete("/products/{id}", nonExistingId).header("Authorization", "Bearer " + accessToken));
		request.andExpect(status().isNotFound());
	}

	@Test
	void deleteShouldDeleteProductWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

		ResultActions request = mockMvc
				.perform(delete("/products/{id}", existingId).header("Authorization", "Bearer " + accessToken));
		request.andExpect(status().isNoContent());
	}

	@Test
	void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions request = mockMvc.perform(put("/products/{id}", existingId).header("Authorization", "Bearer " + accessToken).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		request.andExpect(status().isNoContent());
	}

	@Test
	void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions request = mockMvc
				.perform(put("/products/{id}", nonExistingId).header("Authorization", "Bearer " + accessToken)
						.content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		request.andExpect(status().isNotFound());
	}

	@Test
	void findAllShouldReturnPage() throws Exception {
		ResultActions request = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		request.andExpect(status().isOk());
	}

	@Test
	void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		ResultActions request = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		request.andExpect(status().isOk());
		request.andExpect(jsonPath("$.id").exists());
		request.andExpect(jsonPath("$.name").exists());
		request.andExpect(jsonPath("$.description").exists());
	}

	@Test
	void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() throws Exception {
		ResultActions request = mockMvc
				.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		request.andExpect(status().isNotFound());
	}

}
