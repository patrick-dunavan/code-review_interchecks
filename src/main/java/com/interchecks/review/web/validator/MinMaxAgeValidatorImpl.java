package com.interchecks.review.web.validator;

import static java.util.Objects.nonNull;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.interchecks.review.config.ValidationsConfig;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

//@Component()
@Slf4j
@ToString
public class MinMaxAgeValidatorImpl implements ConstraintValidator<MinMaxAgeValidator, Integer> {

	private int minAge;
	private int maxAge;

	public MinMaxAgeValidatorImpl(ValidationsConfig theValidationsConfig) {
		super();

		minAge = theValidationsConfig.getMinAge();
		maxAge = theValidationsConfig.getMaxAge();

		return;
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {

		final boolean answer = nonNull(value) ? (value >= minAge && value <= maxAge) : false;

		return answer;

	}

	@Override
	public void initialize(MinMaxAgeValidator constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@PostConstruct
	public void postConstruct() {
		log.info("Constructed: " + this);

	}

}
