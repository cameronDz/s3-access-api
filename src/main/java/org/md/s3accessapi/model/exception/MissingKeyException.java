package org.md.s3accessapi.model.exception;

/**
 * exception thrown when a key value is missing from a method call
 * @author cameron
 */
public class MissingKeyException extends Exception {

	private static final long serialVersionUID = 1L;

	public MissingKeyException() {
		super();
	}
	
	public MissingKeyException(String message) {
		super(message);
	}
}
