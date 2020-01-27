package org.md.s3accessapi.model.exception;

public class MissingAwsInformation extends Exception {

	private static final long serialVersionUID = 1L;

	public MissingAwsInformation() {
		super();
	}
	
	public MissingAwsInformation(String message) {
		super(message);
	}
}
