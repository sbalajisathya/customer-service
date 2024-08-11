package com.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * The type Customer service application.
 */
@CrossOrigin
@SpringBootApplication
public class CustomerServiceApplication {

	/**
	 * The entry point of application.
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}
