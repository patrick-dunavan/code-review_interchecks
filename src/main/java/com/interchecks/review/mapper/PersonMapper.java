package com.interchecks.review.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.interchecks.review.dto.PersonDto;
import com.interchecks.review.model.Person;
import static com.interchecks.review.util.Util.*;
import com.interchecks.review.web.request.PersonRequest;

@Component
/**
 * I didn't want to go overboard to pull in OAS/MapStruct, so just emulate for the time being. 
 * 
 *
 */
public final class PersonMapper {

	public Person toPerson(PersonRequest request) { 
	
		final Person p = Person.newInstance(); 
		
		p.setAge(request.getAge());
		p.setFirstName(request.getFirstName());
		p.setLastName(request.getLastName());
		
		return p; 
		
	}
	
	public PersonDto toPersonDto(Person p) { 
		
		final PersonDto pDto = new PersonDto(); 

		pDto.setId(p.getId());

		pDto.setAge(p.getAge());
		pDto.setFirstName(p.getFirstName());
		pDto.setLastName(p.getLastName());

		return pDto; 
		
	}

	public Person toPerson(PersonDto pDto) { 
		
		final Person p = Person.newInstance(); 
		
		p.setId(pDto.getId());
		
		p.setAge(pDto.getAge());
		p.setFirstName(pDto.getFirstName());
		p.setLastName(pDto.getLastName());
		
		return p;
		
	}
	
	public List<PersonDto> toPeople(Collection<Person> personCollection) { 

		final List<PersonDto> returnList = new ArrayList<>(); 
		
		safeCollection(personCollection).forEach( person -> {
			returnList.add(toPersonDto(person));
		});

		return returnList; 
		
	} 
	
	
}
