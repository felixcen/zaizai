package com.zaizai.exceptions;

public class UserServiceException extends RuntimeException {

	 
	public UserServiceException(String errorMessage) {
		super(errorMessage);
	}

	private static final long serialVersionUID = 1123131L;

}
