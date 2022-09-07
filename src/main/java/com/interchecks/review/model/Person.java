package com.interchecks.review.model;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)

public class Person extends IdEntity {
	
    private String firstName;
    private String lastName;
    
    private int age;

    
    public static Person newInstance() { 
    	
    	Person p = new Person(); 
    	
    	//Init...
    	
    	return p; 
    	
    }
    
    
}
