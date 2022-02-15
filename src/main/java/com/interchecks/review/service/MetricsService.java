package com.interchecks.review.service;

import com.interchecks.review.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;

@Service
public class MetricsService {
    @Autowired
    private PersonService personService;

    public OptionalDouble getAverageAgeOfAllPeople()
    {
        return personService.findAll()
                .stream()
                .mapToDouble(Person::getAge)
                .average();
    }
}
