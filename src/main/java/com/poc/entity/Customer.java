package com.poc.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The type Customer.
 */
@Data
@Entity
@Table(name = "CUSTOMER")
@EqualsAndHashCode
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "customer_id")
	private Long id;

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

}
