package com.interchecks.review.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.interchecks.review.dto.PersonDto;
import com.interchecks.review.mapper.PersonMapper;
import com.interchecks.review.model.Person;
import com.interchecks.review.service.MetricsService;
import com.interchecks.review.service.PersonService;
import com.interchecks.review.web.request.RequestScopeInfo;
import com.interchecks.review.web.request.ResponseExceptionHandler;
import com.interchecks.review.web.response.WrappedResponse;

public class PersonControllerTest {

	private PersonController _fixture;

	@Mock
	private RequestScopeInfo _mockRequestScopeInfo;
	@Mock
	private PersonService _mockPersonService;
	@Mock
	private MetricsService _mockMetricsService;

	private ResponseExceptionHandler theResponseExceptionHandler;
	private PersonMapper _personMapper;

	@BeforeEach
	public void beforeEach() {
		
		MockitoAnnotations.openMocks(this);
		
		
		theResponseExceptionHandler = new ResponseExceptionHandler();
		_personMapper = new PersonMapper(); 
		
		_fixture = new PersonController(_mockPersonService, _mockMetricsService, _mockRequestScopeInfo, _personMapper,
				theResponseExceptionHandler);
		
		return;
	}

	private static final String TRANID = "TRANID";

	@Test
	public void test_findPerson_ok() {

		Person p = modelPerson();
		Optional<Person> okOptional = Optional.of(p);

		when(_mockPersonService.findById(100L)).thenReturn(okOptional);
		when(_mockRequestScopeInfo.getTransactionId()).thenReturn(TRANID);

		ResponseEntity<WrappedResponse<PersonDto>> resp = _fixture.findPersonById(100L);

		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(TRANID, resp.getBody().getAuditId());
		
		PersonDto dto = resp.getBody().getData(); 
		
		assertEquals(p.getAge(), dto.getAge());
		assertEquals(p.getFirstName(), dto.getFirstName());
		assertEquals(p.getLastName(), dto.getLastName());

		assertEquals(p.getId().longValue(), dto.getId().longValue());

		return;
	}

	@Test
	public void test_findPerson_notFound() {

		Optional<Person> okOptional = Optional.empty();

		ResponseEntity<WrappedResponse<Person>> retResp = null;
		WrappedResponse<Person> wrapped = WrappedResponse.configureWrappedResponseWith(_mockRequestScopeInfo);
		retResp = ResponseEntity.status(INTERNAL_SERVER_ERROR).body(wrapped);

		when(_mockPersonService.findById(100L)).thenReturn(okOptional);
		when(_mockRequestScopeInfo.getTransactionId()).thenReturn(TRANID);

		ResponseEntity<WrappedResponse<PersonDto>> resp = _fixture.findPersonById(100L);

		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals(TRANID, resp.getBody().getAuditId());
		assertEquals("ID 100 Not Found", resp.getBody().getErrorMessages().get(0));

		assertNotNull(retResp); // make the complier warns happy

	}

	@Test
	public void test_findPerson_exception() {

		ResponseEntity<WrappedResponse<Person>> retResp = null;
		WrappedResponse<Person> wrapped = WrappedResponse.configureWrappedResponseWith(_mockRequestScopeInfo);
		retResp = ResponseEntity.status(INTERNAL_SERVER_ERROR).body(wrapped);

		when(_mockPersonService.findById(100L)).thenThrow(new IllegalArgumentException());
		when(_mockRequestScopeInfo.getTransactionId()).thenReturn(TRANID);

		ResponseEntity<WrappedResponse<PersonDto>> resp = _fixture.findPersonById(100L);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
		assertEquals(TRANID, resp.getBody().getAuditId());
		assertEquals(0, resp.getBody().getErrorMessages().size());

		assertNotNull(retResp); // always make the warnings happy.....

		return;
	}

	private Person modelPerson() {

		Person p = Person.newInstance();
		p.setId(100L);
		p.setFirstName("Dale");
		p.setLastName("Dunavan");
		p.setAge(39);

		return p;

	}

}
