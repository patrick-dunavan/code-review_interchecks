package com.interchecks.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.interchecks.review.exception.GeneralProcessingException;
import com.interchecks.review.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
	
//    Person save(Person person);
//    Optional<Person> findById(Long id);
//    List<Person> findAll();
    
	@Query("SELECT AVG(age) FROM Person p")
	public Double findAverageAge() throws GeneralProcessingException; 
	
}
