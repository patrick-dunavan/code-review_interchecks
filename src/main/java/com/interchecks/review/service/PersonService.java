package com.interchecks.review.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.interchecks.review.model.Person;
import com.interchecks.review.repository.PersonRepository;

@Service
public class PersonService {

	private final PersonRepository personRepository;

	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Person save(Person person) {
		return personRepository.save(person);
	}

	public Optional<Person> findById(Long id) {
		return personRepository.findById(id);
	}

	@Cacheable("people")
	public List<Person> findAll() {
		return personRepository.findAll();
	}

}
