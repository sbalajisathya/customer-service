package com.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * The type Customer dto.
 */
@Data
@AllArgsConstructor
public class CustomerDto {

	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotNull
	private LocalDate dateOfBirth;

}
