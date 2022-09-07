package com.interchecks.review.web.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.interchecks.review.web.request.RequestScopeInfo;

import lombok.Data;

@Data
public class WrappedResponse<T> {

	@JsonInclude(Include.NON_NULL)
	private String auditId; 

	@JsonInclude(Include.NON_NULL)
	private T data; 

	@JsonInclude(Include.NON_EMPTY)
	private List<String> errorMessages; 

	private WrappedResponse() { 
		errorMessages = new ArrayList<>();
	}

	
	public static final<Ins> WrappedResponse<Ins> configureWrappedResponseWith(RequestScopeInfo theRequestScopeInfo) { 
		
		WrappedResponse<Ins> wrappedResponse = new WrappedResponse<Ins>();
		
		//Housekeeping...
		wrappedResponse.setAuditId(theRequestScopeInfo.getTransactionId());
		
		
		return wrappedResponse; 
		
	}
	
	
}
