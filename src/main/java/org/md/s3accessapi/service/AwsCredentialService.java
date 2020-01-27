package org.md.s3accessapi.service;

import org.md.s3accessapi.utility.ValidationUtility;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class AwsCredentialService {

	private String accessKey = "";
	private String secretKey = "";

	public AwsCredentialService() {
		super();
	}

	public AwsCredentialService(String accessKey, String secretKey) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public AWSStaticCredentialsProvider generateAwsCredentialProvider() {
		AWSStaticCredentialsProvider credentialProvider = null;
		try {
			ValidationUtility.validateKeyExists(accessKey, "access");
			ValidationUtility.validateKeyExists(secretKey, "secret");
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			credentialProvider = new AWSStaticCredentialsProvider(credentials);
		} catch (Exception e) {
			// TODO some catch logic
		}
		return credentialProvider;
	}
}
