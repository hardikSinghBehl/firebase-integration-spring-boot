package com.behl.flare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TaskOwnershipViolationException extends ResponseStatusException {

	private static final long serialVersionUID = 3265020831437403636L;
	
	private static final String DEFAULT_MESSAGE = "Access Denied: Insufficient privileges to perform this action.";

	public TaskOwnershipViolationException() {
		super(HttpStatus.FORBIDDEN, DEFAULT_MESSAGE);
	}

}