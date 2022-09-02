package com.interchecks.review.web.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class WrappedResponse<T> {

	public WrappedResponse() { 
		errorMessages = new ArrayList<>();
	}
	
	
	@JsonInclude(Include.NON_NULL)
	private String auditId; 

	@JsonInclude(Include.NON_NULL)
	private T data; 

	@JsonInclude(Include.NON_EMPTY)
	private List<String> errorMessages; 
	
	
	
}
