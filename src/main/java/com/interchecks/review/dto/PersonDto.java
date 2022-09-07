package com.interchecks.review.dto;

import org.springframework.validation.annotation.Validated;

import com.interchecks.review.model.Person;

import lombok.Data;

@Validated
@Data

public class PersonDto {

	private Long id; 
	
    private String firstName;
    private String lastName;
    
    private int age;
    

    public final Person mapTo() { 
    	
    	Person p = Person.newInstance();
    
    	p.setId(id);
    	p.setAge(age);
    	p.setFirstName(firstName);
    	p.setLastName(lastName);
    	
    	return p; 
    	
    }
    
    public static final PersonDto mapFrom(Person p) { 
    	
    	final PersonDto pDto = new PersonDto(); 
    	
    	pDto.setId(p.getId()); 
    	
    	pDto.setAge(p.getAge());
    	pDto.setFirstName(p.getFirstName());
    	pDto.setLastName(p.getLastName());
    	
    	return pDto; 
    	
    }
    
    
}
