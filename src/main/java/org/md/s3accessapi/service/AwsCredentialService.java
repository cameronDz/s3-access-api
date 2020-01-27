package org.md.s3accessapi.service;

import org.md.s3accessapi.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class AwsCredentialService {

	@Value("${s3.access.key}")
	private String accessKey;
	@Value("${s3.secret.key}")
	private String secretKey;

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
		} catch (Exception ex) {
			System.out.println("generateAwsCredentialProvider() - Exception: " + ex.getMessage());
		}
		return credentialProvider;
	}
}
