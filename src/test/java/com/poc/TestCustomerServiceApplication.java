package com.poc;

import com.poc.dto.AppResponse;
import com.poc.dto.CustomerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestCustomerServiceApplication {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CustomerRepository repository;

	@BeforeEach
	public void cleanup() {
		repository.deleteAll();
	}

	@Test
	void testSearchCustomerNotFound() throws Exception {
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.get("/customer-service/api/v1/search-customer"));
		AppResponse resp = objectMapper.readValue(
				response.andReturn().getResponse().getContentAsString(),
				AppResponse.class);
		Assertions.assertEquals(HttpStatus.NOT_FOUND,resp.getStatus() );
		Assertions.assertEquals("CUST-API-1004",resp.getResponseCode() );
		Assertions.assertEquals( "No Record Found!!",resp.getResponseMessage());

	}

	@Test
	void testSearchCustomerSuccess() throws Exception {
		CustomerDto customer = new CustomerDto(1L, "Balaji", "Sathyanarayanan",
				LocalDate.parse("1990-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		mockMvc.perform(
				MockMvcRequestBuilders.post("/customer-service/api/v1/add-customer")
						.content(objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
				"/customer-service/api/v1/search-customer?firstName=Balaji&lastName=Sathyanarayanan"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json")).andReturn();
		String content = result.getResponse().getContentAsString();
		Assertions.assertTrue(content.contains("Balaji"));
		Assertions.assertTrue(content.contains("CUST-API-1003"));
		Assertions.assertTrue(content.contains("OK"));

	}

	@Test
	void testAddCustomerSuccess() throws Exception {
		CustomerDto customer = new CustomerDto(1L, "Balaji", "Sathyanarayanan",
				LocalDate.parse("1990-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders
						.post("/customer-service/api/v1/add-customer")
						.content(objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json")).andReturn();
		String content = result.getResponse().getContentAsString();
		Assertions.assertTrue(content.contains("Customer Successfully Added!!"));
		Assertions.assertTrue(content.contains("CUST-API-1002"));
		Assertions.assertTrue(content.contains("CREATED"));
	}

	@Test
	void testAddCustomerFailed() throws Exception {
		CustomerDto customer = new CustomerDto(1L, "Balaji", "Sathyanarayanan",
				LocalDate.parse("2025-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
						.post("/customer-service/api/v1/add-customer")
						.content(objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json")).andReturn();
		String content = result.getResponse().getContentAsString();
		Assertions.assertTrue(content.contains("CUST-API-ERR-001"));
		Assertions.assertTrue(content.contains("BAD_REQUEST"));

	}

	@Test
	void testAddCustomerAlreadyExists() throws Exception {
		CustomerDto customer = new CustomerDto(1L, "Balaji", "Sathyanarayanan",
				LocalDate.parse("1990-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		mockMvc.perform(
				MockMvcRequestBuilders.post("/customer-service/api/v1/add-customer")
						.content(objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders
						.post("/customer-service/api/v1/add-customer")
						.content(objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType("application/json")).andReturn();
		String content = result.getResponse().getContentAsString();
		Assertions.assertTrue(content.contains("Customer already exists"));
		Assertions.assertTrue(content.contains("CUST-API-1001"));
		Assertions.assertTrue(content.contains("CONFLICT"));
	}

}
