package com.interchecks.review.web.request;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import com.interchecks.review.web.validator.MinMaxAgeValidator;

import lombok.Data;

@Validated
@Data

public class PersonRequest {

	@NotEmpty(message="First name is required")
    private String firstName;

	@NotEmpty(message="Last name is required")
    private String lastName;
	
	@MinMaxAgeValidator
	private Integer age;
    
	
}
