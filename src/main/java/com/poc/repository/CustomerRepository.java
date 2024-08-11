package com.poc.repository;

import com.poc.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * The interface Customer repository.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * Find by first name and last name and date of birth customer.
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param dateOfBirth the date of birth
	 * @return the customer
	 */
	public Customer findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
			String firstName, String lastName, LocalDate dateOfBirth);

}
