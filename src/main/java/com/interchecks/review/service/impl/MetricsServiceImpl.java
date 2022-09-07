package com.interchecks.review.service.impl;

import com.interchecks.review.exception.GeneralProcessingException;
import com.interchecks.review.model.Person;
import com.interchecks.review.service.MetricsService;
import com.interchecks.review.service.PersonService;
import com.interchecks.review.service.enums.ExceptionLevelEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;

@Service
public class MetricsServiceImpl implements MetricsService {
	
	@Autowired
	private PersonService personService;

	
	@Override
	public double getAverageAgeOfAllPeople() throws GeneralProcessingException {

		final double retVal; 
		
		try { 
			retVal = personService.getAverageAge();
			
		} catch (Exception e) { 
			throw new GeneralProcessingException(e.getMessage(), e, ExceptionLevelEnum.PERMANENT);
			
		}

		return retVal; 
		
	}
	
	/**
	 * Plenty of variations here; 
	 * First, if age is definately allowed to be nullable, then we'd need to filter out the people that don't have an age. 
	 * Second, I modified the liquidbase (cool product!) - to disallow null age - and enforced it in the controller/validator //TODO tie thoese rules together
	 * Third - assuming we don't allow null ages, modify the age field to the primitive so we don't Auto-boxing. 
	 * Hmm - or - sql clause to remove non-nulls. 
	 * TODO - An age of "0" - probably not a field we'd want to have the db support anyway - given I'm assuming we're looking for something gt; DOB may be overkill, but...
	 * Fourth - let the database do it --- if we have 1,000,000 records, then returning the entire set into memory to calcuate the average, mem issues will start to potentially arise (and see for "findAll") 
	 * Fifth - JPA doesn't support it (memory serves) - but a reactive model could be looked at
	 * @return
	 */
	public OptionalDouble getAverageAgeOfAllPeopleOld() {
		
		final OptionalDouble retVal = personService.findAll().stream()
//			.filter(aPerson -> null!=aPerson.getAge())
			.mapToDouble(Person::getAge)
			.average();
		
		return retVal;
		
	}
	
	
}
