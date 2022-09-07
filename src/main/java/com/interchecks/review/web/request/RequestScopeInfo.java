package com.interchecks.review.web.request;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Getter;

@Component
@RequestScope

public class RequestScopeInfo {

	@Getter
	private String transactionId;

	@Getter
	private String reqPath; 
	
	public RequestScopeInfo(HttpServletRequest request) {

		transactionId = UUID.randomUUID().toString();

		reqPath = request.getRequestURI(); 

		
	}

}
