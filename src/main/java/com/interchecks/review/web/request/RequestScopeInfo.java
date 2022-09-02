package com.interchecks.review.web.request;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Getter;

@Component
@RequestScope

public class RequestScopeInfo {

	@Getter
	private String transactionId;

	public RequestScopeInfo() {

		transactionId = UUID.randomUUID().toString();

	}

}
