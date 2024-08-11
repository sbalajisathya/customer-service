package com.poc.sevice;

import com.poc.dto.AppResponse;
import com.poc.dto.CustomerDto;
import com.poc.entity.Customer;
import com.poc.repository.CustomerRepository;
import com.poc.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Customer service.
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	private final EntityManager entityManager;

	/**
	 * Instantiates a new Customer service.
	 * @param customerRepository the customer repository
	 * @param entityManager the entity manager
	 */
	public CustomerServiceImpl(CustomerRepository customerRepository,
			EntityManager entityManager) {
		this.customerRepository = customerRepository;
		this.entityManager = entityManager;
	}

	public ResponseEntity<AppResponse> addCustomer(CustomerDto customerDto) {
		AppResponse resp = new AppResponse();

		if (customerRepository
				.findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
						customerDto.getFirstName(), customerDto.getLastName(),
						customerDto.getDateOfBirth()) != null) {
			resp.setStatus(HttpStatus.CONFLICT);
			resp.setResponseCode("CUST-API-1001");
			resp.setResponseMessage(AppConstants.CUSTOMER_ALREADY_EXISTS);
		}
		else {
			Customer customer = new Customer();
			BeanUtils.copyProperties(customerDto, customer, "id");
			customerRepository.save(customer);
			resp.setResponseMessage(
					"ID-" + customer.getId() + " ,Customer Successfully Added!!");
			resp.setStatus(HttpStatus.CREATED);
			resp.setResponseCode("CUST-API-1002");
		}

		return ResponseEntity.status(resp.getStatus()).body(resp);
	}

	@Override
	public ResponseEntity<AppResponse> searchCustomer(Long id, String firstName,
			String lastName, String dateOfBirth) {
		AppResponse resp = new AppResponse();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
		Root<Customer> root = query.from(Customer.class);
		List<Predicate> predicates = new ArrayList<>();

		if (id != null) {
			predicates.add(criteriaBuilder.equal(root.get("id"), id));
		}

		if (StringUtils.isNotBlank(firstName)) {
			predicates.add(criteriaBuilder.like(root.<String>get("firstName"),
					"%" + firstName + "%"));
		}

		if (StringUtils.isNotBlank(lastName)) {
			predicates.add(criteriaBuilder.like(root.<String>get("lastName"),
					"%" + lastName + "%"));
		}

		if (StringUtils.isNotBlank(dateOfBirth)) {
			predicates.add(criteriaBuilder.equal(root.get("dateOfBirth"), LocalDate
					.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
		}

		List<Customer> resultList = entityManager
				.createQuery(
						query.where(predicates.toArray(new Predicate[predicates.size()])))
				.getResultList();
		if (!CollectionUtils.isEmpty(resultList)) {
			resp.setStatus(HttpStatus.OK);
			resp.setResponseCode("CUST-API-1003");
			resp.setResponseMessage(resultList);
		}
		else {
			resp.setResponseCode("CUST-API-1004");
			resp.setStatus(HttpStatus.NOT_FOUND);
			resp.setResponseMessage("No Record Found!!");
		}
		return ResponseEntity.status(resp.getStatus()).body(resp);
	}

}
