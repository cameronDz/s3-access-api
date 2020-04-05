package org.md.api.s3.model.exception;

public class FeatureFlagException extends Exception {

	private static final long serialVersionUID = 1L;

	public FeatureFlagException() {
		super();
	}
	
	public FeatureFlagException(String message) {
		super(message);
	}
}
