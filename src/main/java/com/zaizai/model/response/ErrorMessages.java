package com.zaizai.model.response;

public enum ErrorMessages {

	MISSING_REQUIRED_FIELD("Missing required fields."),	
	RECORD_ALREADY_EXISTS("Record already existis.");	
	
	
	private String errorMessage;

	
	
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	 
}
