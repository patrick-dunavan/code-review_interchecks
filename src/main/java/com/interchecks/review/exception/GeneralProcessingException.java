package com.interchecks.review.exception;

import com.interchecks.review.service.enums.ExceptionLevelEnum;

@SuppressWarnings("serial")
public class GeneralProcessingException extends Exception {


	private ExceptionLevelEnum _exceptionLevel; 
	
	
	public GeneralProcessingException(String message, ExceptionLevelEnum exceptionLevel) { 

		super(message);
		_exceptionLevel = exceptionLevel;
		
	}
	
	public GeneralProcessingException(String message, Throwable cause, ExceptionLevelEnum exceptionLevel) {
		
		super(message, cause);
		_exceptionLevel = exceptionLevel;
		
	}

	@Override
	public String toString() {
		
		StringBuilder msg = new StringBuilder(super.toString()); 
		msg.append(String.format("(ExceptionLevel=%s", _exceptionLevel)); 
		
		return msg.toString(); 
		
	}

	
	public ExceptionLevelEnum getExceptionLevel() { 
		return _exceptionLevel;
	}
	
	
}
