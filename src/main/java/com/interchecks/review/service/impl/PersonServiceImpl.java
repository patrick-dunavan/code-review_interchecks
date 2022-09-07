package com.interchecks.review.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.interchecks.review.exception.GeneralProcessingException;
import com.interchecks.review.model.Person;
import com.interchecks.review.repository.PersonRepository;
import com.interchecks.review.service.PersonService;
import static com.interchecks.review.service.enums.ExceptionLevelEnum.*;
import com.interchecks.review.service.enums.PersonAttributesEnum;
import com.interchecks.review.util.Util;

@Service
public class PersonServiceImpl implements PersonService {

	private final PersonRepository personRepository;

	public PersonServiceImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	
	@Override
	public void save(Person person) {
		personRepository.save(person);
	}

	@Override
	public Optional<Person> findById(Long id) {
		return personRepository.findById(id);
	}

	//TODO, based off data.
	//TODO - make pagabale(?); what's the use case for "find all?"
	@Override
	@Cacheable("people") 
	public List<Person> findAll() {
		return personRepository.findAll();
	}

	
	
	@Override
	public double getAverageAge() throws GeneralProcessingException {
		
		final Double retVal; 
		
		try { 
			retVal = personRepository.findAverageAge();
			
		} catch (Exception e) { 
			throw new GeneralProcessingException(Util.fmtStr("Cannot calculate"), PERMANENT);
		}

		
		return retVal;
		
	}

	
	/**
	 * My composite methods almost always try/throw checked Exceptions;
	 */
	@Override
	public Map<PersonAttributesEnum, Boolean> getVerifications(Long id) throws GeneralProcessingException {
		
		Map<PersonAttributesEnum, Boolean> retMap = new HashMap<>(); 
		
		Optional<Person> optPerson = findById(id);
		
		
		if (optPerson.isEmpty()) { 
			throw new GeneralProcessingException(Util.fmtStr("ID %s not found", id), PERMANENT);
		}

		
		Person p = optPerson.get();
		
		PersonAttributesEnum.allAttributes().forEach( anEnum -> {
			retMap.put(anEnum, anEnum.passes(p));
		});
		
		return retMap; 
		
	}
	
	
	
}
