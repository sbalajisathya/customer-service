package com.poc.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * The type Api response.
 */
@Data
public class AppResponse {

	private HttpStatus status;

	private String responseCode;

	private Object responseMessage;

}
