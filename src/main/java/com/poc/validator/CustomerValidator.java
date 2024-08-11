package com.poc.validator;

import com.poc.util.AppConstants;
import com.poc.dto.AppResponse;
import com.poc.dto.CustomerDto;
import com.poc.dto.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Customer validator.
 */
@Slf4j
@Component
public class CustomerValidator {

	private final MessageSource messageSource;

	/**
	 * Instantiates a new Customer validator.
	 * @param messageSource the message source
	 */
	public CustomerValidator(MessageSource messageSource) {

		this.messageSource = messageSource;
	}

	/**
	 * Gets errors.
	 * @param bindingResult the binding result
	 * @return the errors
	 */
	public ResponseEntity<AppResponse> getErrors(BindingResult bindingResult) {
		AppResponse resp = new AppResponse();
		if (bindingResult.hasErrors()) {

			Map<String, List<String>> fieldErrorMap = bindingResult.getFieldErrors()
					.stream().collect(
							Collectors
									.groupingBy(FieldError::getField,
											Collectors.mapping(
													e -> messageSource.getMessage(e,
															LocaleContextHolder
																	.getLocale()),
													Collectors.toList())));

			ErrorDetail error = new ErrorDetail();
			error.setErrorMessage(AppConstants.INPUT_VALIDATION_FAIL);
			error.setErrorDetails(fieldErrorMap);
			resp.setResponseMessage(error);
			resp.setResponseCode("CUST-API-ERR-001");
			resp.setStatus(HttpStatus.BAD_REQUEST);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
		}
		return null;
	}

	/**
	 * Validate.
	 * @param customerDto the customer dto
	 * @param bindingResult the binding result
	 */
	public void validate(CustomerDto customerDto, BindingResult bindingResult) {
		if ((LocalDate.now()).isBefore(customerDto.getDateOfBirth())) {
			FieldError fieldError = new FieldError(AppConstants.CUSTOMER, "dateOfBirth",
					"dateOfBirth should not be in Future Date!");
			bindingResult.addError(fieldError);
		}
	}

}
