package com.interchecks.review.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.interchecks.review.exception.GeneralProcessingException;
import com.interchecks.review.model.Person;
import com.interchecks.review.service.enums.PersonAttributesEnum;

public interface PersonService {

	public void save(Person person);

	public Optional<Person> findById(Long id);

	public List<Person> findAll();

	
	/* ------------------------- */
	/* Derived/composite methods */ 
	/* ------------------------- */
	
	public double getAverageAge() throws GeneralProcessingException;

	public Map<PersonAttributesEnum, Boolean> getVerifications(Long id) throws GeneralProcessingException;
	
	
}