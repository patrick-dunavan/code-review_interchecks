package com.interchecks.review.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.interchecks.review.util.Util.*;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties(prefix = "com.interchecks.validations")

@Data
@Slf4j

public class ValidationsConfig {

	private int minAge;
	private int maxAge;

	
	@PostConstruct
	public void postConstruct() {

		infoIf(log, () -> "Created: " + this);

		return; // TODO --- I know, superfluous; it's kinda my style... unless java has changed, the return is implicit to do and RTS...
	}


}
