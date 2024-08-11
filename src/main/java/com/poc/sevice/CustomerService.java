package com.poc.sevice;

import com.poc.dto.AppResponse;
import com.poc.dto.CustomerDto;
import org.springframework.http.ResponseEntity;

/**
 * The interface Customer service.
 */
public interface CustomerService {

	/**
	 * Add customer response entity.
	 * @param customerDto the customer dto
	 * @return the response entity
	 */
	ResponseEntity<AppResponse> addCustomer(CustomerDto customerDto);

	/**
	 * Search customer response entity.
	 * @param id the id
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param dateOfBirth the date of birth
	 * @return the response entity
	 */
	ResponseEntity<AppResponse> searchCustomer(Long id, String firstName, String lastName,
			String dateOfBirth);

}
