package com.poc.controller;

import com.poc.dto.AppResponse;
import com.poc.dto.CustomerDto;
import com.poc.sevice.CustomerService;
import com.poc.validator.CustomerValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The type Customer controller.
 */
@RestController
@RequestMapping("/customer-service/api/v1")
@Slf4j
@Tag(name = "CustomerApi", description = "Endpoints for Customer Service Api")
public class CustomerController {

	private final CustomerService customerService;

	private final CustomerValidator validator;

	/**
	 * Instantiates a new Customer controller.
	 * @param customerService the customer service
	 * @param validator the validator
	 */
	public CustomerController(CustomerService customerService,
			CustomerValidator validator) {
		this.customerService = customerService;
		this.validator = validator;
	}

	/**
	 * Add customer response entity.
	 * @param customerDto the customer dto
	 * @param bindingResult the binding result
	 * @return the response entity
	 */
	@Operation(summary = "Add Customer", description = "Save Customer Data")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "CUST-API-1002, ID-XX, Customer Successfully Added!!"),
			@ApiResponse(responseCode = "400", description = "CUST-API-ERR-001, Bad Request, Input Validation Errors!"),
			@ApiResponse(responseCode = "409", description = "CUST-API-1001, Customer already exists!") })
	@PostMapping(value = "/add-customer")
	public ResponseEntity<AppResponse> addCustomer(
			@Valid @RequestBody CustomerDto customerDto, BindingResult bindingResult) {
		log.debug("AddCustomer - Method started!!");
		validator.validate(customerDto, bindingResult);
		if (bindingResult.hasErrors()) {
			return validator.getErrors(bindingResult);
		}
		return customerService.addCustomer(customerDto);
	}

	/**
	 * Search customer response entity.
	 * @param id the id
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param dateOfBirth the date of birth
	 * @return the response entity
	 */
	@Operation(summary = "Search Customer", description = "Provides search customer list")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "CUST-API-1003, Success customer search list"),
			@ApiResponse(responseCode = "404", description = "CUST-API-1004, No Record Found!!") })
	@GetMapping(path = "/search-customer")
	public ResponseEntity<AppResponse> searchCustomer(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String dateOfBirth) {
		log.debug("SearchCustomer - Method started!!");
		return customerService.searchCustomer(id, firstName, lastName, dateOfBirth);
	}

}
