package com.interchecks.review.service;

import com.interchecks.review.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;

@Service
public class MetricsService {
	
	@Autowired
	private PersonService personService;

	
	public OptionalDouble getAverageAgeOfAllPeople() {
		
		final OptionalDouble retVal = personService.findAll().stream()
			.filter(aPerson -> null!=aPerson.getAge())
			.mapToDouble(Person::getAge)
			.average();
		
		return retVal;
		
	}
	
	
}
