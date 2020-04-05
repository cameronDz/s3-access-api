package org.md.api.s3.model.exception;

public class MissingAwsInformation extends Exception {

	private static final long serialVersionUID = 1L;

	public MissingAwsInformation() {
		super();
	}
	
	public MissingAwsInformation(String message) {
		super(message);
	}
}
