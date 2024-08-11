package com.poc.exception;

import com.poc.dto.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<AppResponse> requestParseExceptionHandler(
			HttpMessageNotReadableException ex) {
		AppResponse resp = new AppResponse();
		resp.setResponseMessage("Customer Api Failed, Please check with support team!");
		resp.setResponseCode("CUST-API-ERR-002");
		resp.setStatus(HttpStatus.BAD_REQUEST);
		log.error("requestParseExceptionHandler Exception :{}",
				ExceptionUtils.getFullStackTrace(ex));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<AppResponse> genericExceptionHandler(Exception ex) {
		AppResponse resp = new AppResponse();
		resp.setResponseMessage("Customer Api Failed, Please check with support team!");
		resp.setResponseCode("CUST-API-ERR-002");
		resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		log.error("genericExceptionHandler Exception :{}",
				ExceptionUtils.getFullStackTrace(ex));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}

}
