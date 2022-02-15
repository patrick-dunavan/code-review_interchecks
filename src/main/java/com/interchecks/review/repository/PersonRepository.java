package com.interchecks.review.repository;

import com.interchecks.review.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person save(Person person);
    Optional<Person> findById(Long id);
    List<Person> findAll();
}
