package com.interchecks.review.web.controller;

import com.interchecks.review.model.Metrics;
import com.interchecks.review.model.Person;
import com.interchecks.review.service.MetricsService;
import com.interchecks.review.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    public final PersonService personService;

    @Autowired
    public MetricsService metricsService;

    public PersonController(PersonService personService){this.personService = personService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<Metrics> getMetrics(){
        Double averageAge = metricsService.getAverageAgeOfAllPeople().orElse(0);
        Metrics metrics = new Metrics();
        metrics.setAverageAge(averageAge);
        return ResponseEntity.ok(metrics);}

    @GetMapping("/create")
    public ResponseEntity savePerson(@RequestParam String firstName, @RequestParam String lastName){
        Person newPerson = new Person();
        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        Person savedPerson = personService.save(newPerson);
        URI location = URI.create(String.format("/person/%s", savedPerson.getId()));
        return ResponseEntity.created(location).body(savedPerson);
    }

    @GetMapping("/{id}"
    )
    public ResponseEntity<Person> getPersonById(@PathVariable Long id){
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/verify"
    )
    public ResponseEntity<Boolean> isOverTwentyOne(@PathVariable Long id){
        try {
            Person foundPerson = personService.findById(id).get();
            return ResponseEntity.ok(foundPerson.getAge() >= 21);
        } catch(Exception e){
            System.exit(1);
        }
        return ResponseEntity.ok(false);
    }


    @GetMapping
    public ResponseEntity<List<Person>> getPeople(){
        return ResponseEntity.ok().body(personService.findAll());
    }
}
