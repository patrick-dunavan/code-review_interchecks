package com.interchecks.review.web.request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import static com.interchecks.review.util.Util.*;
import com.interchecks.review.web.response.WrappedResponse;

@Component
public class ResponseExceptionHandler {

	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedException(WrappedResponse<T> theWrappedResponse, BindingResult results) { 
	
		safeCollection(results.getFieldErrors()).forEach( err -> {
			
			theWrappedResponse.getErrorMessages().add(err.getDefaultMessage());
		});
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(theWrappedResponse);

	}
	
	
}
