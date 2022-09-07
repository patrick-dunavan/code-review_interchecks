package com.interchecks.review.web.controller;

import static com.interchecks.review.service.enums.PersonAttributesEnum.IS_21;
import static com.interchecks.review.util.Util.debugIf;
import static com.interchecks.review.util.Util.fmtStr;
import static com.interchecks.review.util.Util.isNot;
import static com.interchecks.review.web.response.WrappedResponse.configureWrappedResponseWith;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.interchecks.review.dto.PersonDto;
import com.interchecks.review.exception.GeneralProcessingException;
import com.interchecks.review.mapper.PersonMapper;
import com.interchecks.review.model.Metrics;
import com.interchecks.review.model.Person;
import com.interchecks.review.service.MetricsService;
import com.interchecks.review.service.PersonService;
import com.interchecks.review.service.enums.PersonAttributesEnum;
import com.interchecks.review.web.request.PersonRequest;
import com.interchecks.review.web.request.RequestScopeInfo;
import com.interchecks.review.web.request.ResponseExceptionHandler;
import com.interchecks.review.web.response.WrappedResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@RestController
@RequestMapping("/person")

public class PersonController {

//	@Autowired When in Rome; I have not reviewed best pratices for testing/mock private dependencies w/o reflection;  
//	constructor-based injection, here we go

	/* ------------------------ */
	/* Interceptors and Mappers */
	/* ------------------------ */
	
	private RequestScopeInfo _requestScopeInfo;
	private PersonMapper _personMapper; 
	
	/* -------- */
	/* Services */
	/* -------- */
	private PersonService _personService;
	private MetricsService _metricsService;

	/* -------------------------- */
	/* Baseline exception handler */
	/* -------------------------- */
	private ResponseExceptionHandler _responseExceptionHandler;

	
	/* ----------- */
	/* Constructor */
	/* ----------- */
	public PersonController(
			PersonService personService,
			MetricsService metricsService,
			RequestScopeInfo requestScopeInfo,
			PersonMapper personMapper,
			ResponseExceptionHandler responseExceptionHandler
			) 
	{
		_personService = personService; 
		_metricsService = metricsService;
		_requestScopeInfo = requestScopeInfo;
		_personMapper = personMapper;
		_responseExceptionHandler = responseExceptionHandler; 
	}

	
	/* -------------------------------------------------------------- */
	/* methods                                                        */
	/* -------------------------------------------------------------- */
	
	/**
	 * My modified three-step (usually, 4 if I get the time --- In theory, any exception thrown out of this layer will create a "ProcessFailedException" - or a variant 
	 * At this point, any exception will be caught and dealt with generally; ProcessFailedException / variant normally would have a public-facing set of errors 
	 * and a private configuration / logging, etc. - to expose internals. 
	 * @param thePersonRequest
	 * @param theBindingResult
	 * @return
	 */
	@PostMapping(path="/create", consumes={"application/json", "application/json-v1"})
	public ResponseEntity<WrappedResponse<PersonDto>> savePerson(
			@Valid @RequestBody PersonRequest thePersonRequest,
			BindingResult theBindingResult) {

		final String methodName = "savePerson";
		
		ResponseEntity<WrappedResponse<PersonDto>> theResponseEntity;
		final WrappedResponse<PersonDto> theResponse = configureWrappedResponseWith(_requestScopeInfo);
		
		try { 

			debugIf(log, () -> "Entering: " + methodName);
			
			final boolean errorFree = isNot(theBindingResult.hasErrors());

			/*-------------------------*/
			/** Happy Processing flow **/
			/*-------------------------*/
			if (errorFree) { 

				Person newPerson = _personMapper.toPerson(thePersonRequest);
				
				debugIf(log, () -> "Person to save: " + newPerson);
		
				_personService.save(newPerson);
		
				PersonDto pDto = _personMapper.toPersonDto(newPerson); 
				
				// Sets the header of the original caller.
				URI location = URI.create(fmtStr("/person/%s", pDto.getId()));  
		
				theResponse.setData(pDto);
				theResponseEntity = ResponseEntity.created(location).body(theResponse);
				
			} else {
				theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, theBindingResult);
			}
			
			
		} catch (Exception e) { /* In theory, never happens but don't have service level-exceptions configured */ 
			theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, theBindingResult);
			
		}  finally { 
			debugIf(log, () -> "Exiting: " + methodName);
			
		}

		return theResponseEntity;

	}

	
	@GetMapping("/{id}")
	public ResponseEntity<WrappedResponse<PersonDto>> findPersonById(@Validated @PathVariable long id) {

		final String methodName = "getPersonById";
		
		ResponseEntity<WrappedResponse<PersonDto>> theResponseEntity;
		final WrappedResponse<PersonDto> theResponse = configureWrappedResponseWith(_requestScopeInfo);
		
		try { 
			
			debugIf(log, () -> "Entering: " + methodName);

			Optional<Person> personOpt = _personService.findById(id);
			
			if (personOpt.isPresent()) { 
				theResponse.setData(_personMapper.toPersonDto(personOpt.get()));
				theResponseEntity = ResponseEntity.ok(theResponse);
			} else { 
				theResponseEntity = _responseExceptionHandler.handleProcessingFailedNotFound(theResponse, fmtStr("ID %s Not Found", id), false);
			}
			
			
		} catch (Exception e) { 
			theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, e, false);
			
		} finally { 
			debugIf(log, () -> "Exiting: " + methodName);
			
		}
		
		return theResponseEntity; 
		
	}

	
	/**
	 * returns metrics
	 * @return
	 */	
	@GetMapping("/metrics")
	public ResponseEntity<WrappedResponse<Metrics>> getMetrics() {

		final String methodName = "getMetrics";

		ResponseEntity<WrappedResponse<Metrics>> theResponseEntity = null;
		final WrappedResponse<Metrics> theResponse = configureWrappedResponseWith(_requestScopeInfo);

		try { 
			
			debugIf(log, () -> "Entering: " + methodName);

			Double averageAge = _metricsService.getAverageAgeOfAllPeople();
			Metrics metrics = Metrics.builder().averageAge(averageAge).build();
			
			theResponse.setData(metrics);
			theResponseEntity = ResponseEntity.ok().body(theResponse);
			
			
		} catch (GeneralProcessingException gpe) { 
			theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, gpe, false);
			
		} catch (Exception e) { 
			theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, e, false);
			
		} finally {
			debugIf(log, () -> "Exiting: " + methodName);
			
		}
		
		return theResponseEntity;

	}

	
	/**
	 * Something like this probably has rules around it (Can a person, in this state --- do "something?")
	 *  
	 * 
	 * @param id
	 * @param theBindingResult
	 * @return
	 */
	@GetMapping("/{id}/verify")
	public ResponseEntity<WrappedResponse<Map<PersonAttributesEnum, Boolean>>> verifications(@PathVariable long id) {

		final Map<PersonAttributesEnum, Boolean> verificationsMap;
		
		final String methodName = "ageVerifications";
		
		ResponseEntity<WrappedResponse<Map<PersonAttributesEnum, Boolean>>> theResponseEntity;
		final WrappedResponse<Map<PersonAttributesEnum, Boolean>> theResponse = configureWrappedResponseWith(_requestScopeInfo);

		try {
			
			debugIf(log, () -> "Entering: " + methodName);

			verificationsMap = _personService.getVerifications(id);
			
			theResponse.setData(verificationsMap);
			theResponseEntity = ResponseEntity.ok(theResponse);

		} catch (Exception e) {
			theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, e, true);
			
		} finally {
			debugIf(log, () -> "Exiting: " + methodName);
			
		}

		return theResponseEntity; 
		
	}

	
	@GetMapping("/listAll")
	public ResponseEntity<WrappedResponse<List<PersonDto>>> getPeople() {
		
		final String methodName = "getPeople";
		
		ResponseEntity<WrappedResponse<List<PersonDto>>> theResponseEntity;
		final WrappedResponse<List<PersonDto>> theResponse = configureWrappedResponseWith(_requestScopeInfo);
		
		
		try {
			
			debugIf(log,  () -> "Entering: " + methodName);
			
			List<PersonDto> dtoList = _personMapper.toPeople(_personService.findAll());
		
			theResponse.setData(dtoList);
			theResponseEntity = ResponseEntity.ok(theResponse);			
			
			
		} catch (Exception e) {
			theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, e, true);
			
		} finally { 
			debugIf(log, () -> "Exiting: " + methodName);
			
		}
		
		return theResponseEntity; 
		
	}

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<WrappedResponse<Object>> handleConstraintViolationException(MethodArgumentTypeMismatchException matme) {
    	
		final WrappedResponse<Object> theResponse = configureWrappedResponseWith(_requestScopeInfo);
		theResponse.getErrorMessages().add("The uri is invalid: " + _requestScopeInfo.getReqPath());

		
        return new ResponseEntity<WrappedResponse<Object>>(theResponse, BAD_REQUEST );
        
    }
    
    
    
    
	@GetMapping("/{id}/verify21Plus")
	public ResponseEntity<WrappedResponse<Boolean>> isOverTwentyOne(@PathVariable long id) {

		final String methodName = "verify21Plus";
		
		ResponseEntity<WrappedResponse<Boolean>> theResponseEntity;
		final WrappedResponse<Boolean> theResponse = configureWrappedResponseWith(_requestScopeInfo);

		try {
			
			debugIf(log, () -> "Entering: " + methodName);
			
			Optional<Person> personOpt = _personService.findById(id);
			
			if (personOpt.isPresent()) { 
				theResponse.setData(IS_21.passes(personOpt.get()));
				theResponseEntity = ResponseEntity.ok(theResponse);
			} else { 
				theResponseEntity = _responseExceptionHandler.handleProcessingFailedNotFound(theResponse, fmtStr("ID %s Not Found", id), false);
			}
				
			
		} catch (Exception e) {
			theResponseEntity = _responseExceptionHandler.handleProcessingFailedException(theResponse, e, true);
			
		} finally {
			debugIf(log, () -> "Exiting: " + methodName);
			
		}

		return theResponseEntity; 
		
	}
    
	/** ----------------------------------------------- **/
	/** OLD Methods Keeping around for any conversation **/ 
	/** ----------------------------------------------- **/

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


	
//	@GetMapping("/{id}/verifyOld")
//	public ResponseEntity<Boolean> isOverTwentyOneOld(@PathVariable Long id) {
//
//		try {
//			Person foundPerson = personService.findById(id).get();
//			
//			return ResponseEntity.ok(foundPerson.getAge() >= 21);
//			
//		} catch (Exception e) {
//			
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//
//		return ResponseEntity.ok(false);
//
//	}

//	// TODO Add security
//	@GetMapping("/metricsOld")
//	public ResponseEntity<Metrics> getMetricsOld() {
//
//		Double averageAge = metricsService.getAverageAgeOfAllPeopleOld().orElse(0);
//
//		Metrics metrics = Metrics.builder().averageAge(averageAge).build();
////		metrics.setAverageAge(averageAge);
//
//		return ResponseEntity.ok(metrics);
//
//	}


}
