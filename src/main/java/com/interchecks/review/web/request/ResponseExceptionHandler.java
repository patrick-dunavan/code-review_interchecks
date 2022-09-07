package com.interchecks.review.web.request;

import static com.interchecks.review.util.Util.safeCollection;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.interchecks.review.exception.GeneralProcessingException;
import com.interchecks.review.service.enums.ExceptionLevelEnum;
import com.interchecks.review.web.response.WrappedResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

public class ResponseExceptionHandler {

	
	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedException(WrappedResponse<T> theWrappedResponse, BindingResult results, HttpStatus returnStatus) {

		safeCollection(results.getFieldErrors()).forEach( err -> {
			theWrappedResponse.getErrorMessages().add(err.getDefaultMessage());
		});
		
		return ResponseEntity.status(returnStatus).body(theWrappedResponse);

	}
	
	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedException(WrappedResponse<T> theWrappedResponse, BindingResult results) { 
		
		return handleProcessingFailedException(theWrappedResponse, results, HttpStatus.BAD_REQUEST);
	}

	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedException(WrappedResponse<T> theWrappedResponse, GeneralProcessingException gpe, boolean exposeDetails) {
		
		log.error(gpe.getMessage());
		
		if (exposeDetails) { 
			theWrappedResponse.getErrorMessages().add(gpe.getMessage());
		}
		
		HttpStatus derviedStatus = gpe.getExceptionLevel()==ExceptionLevelEnum.PERMANENT ? INTERNAL_SERVER_ERROR : SERVICE_UNAVAILABLE;
		
		return ResponseEntity.status(derviedStatus).body(theWrappedResponse);
		
	}

	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedException(WrappedResponse<T> theWrappedResponse, Exception e, boolean exposeDetails) {
		
		log.error(e.getMessage());
		
		if (exposeDetails) { 
			theWrappedResponse.getErrorMessages().add(e.getMessage());
		}
		
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(theWrappedResponse);
		
	}

	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedNotFound(WrappedResponse<T> theWrappedResponse, boolean exposeDetails) {
		return ResponseEntity.status(NOT_FOUND).body(theWrappedResponse);
	}

	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedNotFound(WrappedResponse<T> theWrappedResponse, String message, boolean exposeDetails) {
		return handleProcessingFailedNotFound(theWrappedResponse, Arrays.asList(message), exposeDetails);
	}
	

	public <T> ResponseEntity<WrappedResponse<T>> handleProcessingFailedNotFound(WrappedResponse<T> theWrappedResponse, List<String> messages, boolean exposeDetails) {
		
		safeCollection(messages).forEach(msg -> {
			theWrappedResponse.getErrorMessages().add(msg);
		});
		
		return ResponseEntity.status(NOT_FOUND).body(theWrappedResponse);
	}

	
}
