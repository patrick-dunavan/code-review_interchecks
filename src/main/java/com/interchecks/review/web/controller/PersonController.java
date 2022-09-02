package com.interchecks.review.web.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interchecks.review.model.Metrics;
import com.interchecks.review.model.Person;
import com.interchecks.review.service.MetricsService;
import com.interchecks.review.service.PersonService;
import com.interchecks.review.web.request.PersonRequest;
import com.interchecks.review.web.request.RequestScopeInfo;
import com.interchecks.review.web.request.ResponseExceptionHandler;
import com.interchecks.review.web.response.WrappedResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@RestController
@RequestMapping("/person")

public class PersonController {

	/* ------------ */
	/* Interceptors */ 
	/* ------------ */
	
	@Autowired
	private RequestScopeInfo _requestScopeInfo; 

	/* -------- */
	/* Services */ 
	/* -------- */
	@Autowired
	private PersonService personService;

	@Autowired
	private MetricsService metricsService;


	/* -------------------------- */
	/* Baseline exception handler */ 
	/* -------------------------- */
	@Autowired
	private ResponseExceptionHandler _responseExceptionHandler; 


	/* ----------- */
	/* Constructor */ 
	/* ----------- */
	public PersonController() {
	}

	
	
	//TODO Add security
	@GetMapping("/metrics")
	public ResponseEntity<Metrics> getMetrics() {
		
		Double averageAge = metricsService.getAverageAgeOfAllPeople().orElse(0);
		
		Metrics metrics = Metrics.builder().averageAge(averageAge).build();
//		metrics.setAverageAge(averageAge);
		
		return ResponseEntity.ok(metrics);
		
	}

	@PostMapping(consumes={"application/json", "application/json-v1"}, path="/create")
	public ResponseEntity<WrappedResponse<Person> > savePerson(
			@Valid @RequestBody PersonRequest thePersonRequest, BindingResult theBindingResult) {

		ResponseEntity<WrappedResponse<Person>> theResponseEntity = null; 
		WrappedResponse<Person> theResponse = new WrappedResponse<>(); 
		
		theResponse.setAuditId(_requestScopeInfo.getTransactionId());
		
		if (theBindingResult.hasErrors()) { 
			return _responseExceptionHandler.handleProcessingFailedException(theResponse, theBindingResult);
		}
		
		
		Person newPerson = new Person();
		
		newPerson.setFirstName(thePersonRequest.getFirstName()); 
		newPerson.setLastName(thePersonRequest.getLastName()); 
		newPerson.setAge(thePersonRequest.getAge()); 
		
		log.debug("Person to save: " + newPerson); 
		
		Person savedPerson = personService.save(newPerson);
		
		//Sets the header of the original caller.
		URI location = URI.create(String.format("/person/%s", savedPerson.getId()));
		
		theResponse.setData(savedPerson); 
		theResponseEntity = ResponseEntity.created(location).body(theResponse);
		
		return theResponseEntity;
		
	}
	
//	@GetMapping("/create")
//	public ResponseEntity savePerson(@RequestParam String firstName, @RequestParam String lastName) {
//		
//		Person newPerson = new Person();
//		newPerson.setFirstName(firstName);
//		newPerson.setLastName(lastName);
//		Person savedPerson = personService.save(newPerson);
//		URI location = URI.create(String.format("/person/%s", savedPerson.getId()));
//		return ResponseEntity.created(location).body(savedPerson);
//		
//	}

	@GetMapping("/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
		
		return personService.findById(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
		
	}

	
	@GetMapping("/{id}/verify")
	public ResponseEntity<Boolean> isOverTwentyOne(@PathVariable Long id) {
		
		try {
			Person foundPerson = personService.findById(id).get();
			return ResponseEntity.ok(foundPerson.getAge() >= 21);
		} catch (Exception e) {
			System.exit(1);
		}
		
		return ResponseEntity.ok(false);
		
	}

	@GetMapping
	public ResponseEntity<List<Person>> getPeople() {
		
		return ResponseEntity.ok().body(personService.findAll());
		
	}
	
}
