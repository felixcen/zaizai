package com.zaizai.model.response;

public class OperationStatusModel {
	
	
	public OperationStatusModel() {
		
	}
	
	public OperationStatusModel(String operationResult, String operationName) {
		this.operationResult = operationResult;
		this.operationName = operationName;
	}

	private String operationResult;
	private String operationName;

	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

}
