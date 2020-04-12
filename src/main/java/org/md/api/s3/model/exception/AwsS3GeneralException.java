package org.md.api.s3.model.exception;

public class AwsS3GeneralException extends Exception {

    private static final long serialVersionUID = 1L;

    public AwsS3GeneralException() {
        super();
    }

    public AwsS3GeneralException(String message) {
        super(message);
    }
}
