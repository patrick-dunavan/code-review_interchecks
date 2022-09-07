package com.interchecks.review.service.enums;

import java.util.EnumSet;
import java.util.function.Function;

import com.interchecks.review.model.Person;

/**
 * I could inject in these values via setters, then close them off; for now, as-is.
 *
 */
public enum PersonAttributesEnum {

	/******************************************************************/ 

	IS_18(person -> null!=person && person.getAge()>=18, "valid 18+"), 
	IS_21(person -> null!=person && person.getAge()>=21, "valid 21+"),
	
	;
	
	/******************************************************************/ 
	
	private Function<Person, Boolean> _functor; 
	private String _description; 

	
	private PersonAttributesEnum(Function<Person,Boolean> functor, String description) {
		_functor = functor;
		_description = description;
	}

	
	public boolean passes(Person p) { 
		boolean retVal = _functor.apply(p);
		return retVal; 
	}

	public String getDescription() {
		return _description;
	}


	
	/* TODO - This could be used later with a reduction/fitlering clause; just keeping as-is */ 
	public static EnumSet<PersonAttributesEnum> allAttributes() { 
		return EnumSet.allOf(PersonAttributesEnum.class);
	}
	
	
	
}
