package com.interchecks.review.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties(prefix="com.interchecks.validations")

@Data
@Slf4j

public class ValidationsConfig {

	private int minAge;
	private int maxAge; 
	

	@PostConstruct
	public void postConstruct() { 

		log.info("Created: " + this);
		
	}
}
