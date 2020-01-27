package org.md.s3accessapi.service;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3ClientService {

	private String accessKey = "";
	private String secretKey = "";
	private Regions region = Regions.US_EAST_1;
	
	
	public S3ClientService() {
		super();
	}
	
	public S3ClientService(String accessKey, String secretKey) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public List<String> getS3BucketContent(String bucketName) {
		List<String> list = null;
		try {
			 ObjectListing objectList = configuredS3Client().listObjects(bucketName);
			 list = new ArrayList<String>();
			 for (S3ObjectSummary summary : objectList.getObjectSummaries()) {
				 list.add(summary.getKey());
			 }
		} catch (Exception e) {
			// TODO handle exception
		}
		return list;
	}
	
	public boolean doesS3BucketExist(String name) {
		boolean doesExist = false;
		configuredS3Client().doesBucketExistV2(name);
		return doesExist;
	}
	
	private AmazonS3 configuredS3Client() {
		AmazonS3 s3Client = null;
		try {
			AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
			builder.withCredentials(generateAwsCredentialProvider());
			builder.withRegion(region);
			s3Client = builder.build();
		} catch (Exception e) {
			// TODO some catch logic
		}
		return s3Client;
	}
	
	private AWSStaticCredentialsProvider generateAwsCredentialProvider() {
		AWSStaticCredentialsProvider credentialProvider = null;
		try {
			validateKeyExists(accessKey, "access");
			validateKeyExists(secretKey, "secret");
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			credentialProvider = new AWSStaticCredentialsProvider(credentials);
		} catch (Exception e) {
			// TODO some catch logic
		}
		return credentialProvider;
	}
	
	private void validateKeyExists(String key, String name) throws Exception {
		if (isStringNullOrEmpty(key)) {
			String message = "Invalid key: " + name;
			throw new Exception(message);
		}
	}
	
	private boolean isStringNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
}
