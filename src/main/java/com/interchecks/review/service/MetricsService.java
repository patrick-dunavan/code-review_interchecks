package com.interchecks.review.service;

import com.interchecks.review.exception.GeneralProcessingException;

public interface MetricsService {

	public double getAverageAgeOfAllPeople() throws GeneralProcessingException;

}