package org.md.s3accessapi.service;

import java.util.ArrayList;
import java.util.List;

import org.md.s3accessapi.model.exception.MissingAwsInformation;
import org.md.s3accessapi.utility.InputStreamUtility;
import org.md.s3accessapi.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3ClientService {

	@Value("${s3.bucket.region}")
	private String region;

	private AwsCredentialService credentialsService = new AwsCredentialService();

	public S3ClientService() {
		super();
	}

    public List<String> getS3BucketContentList(String bucketName) {
		List<String> list = null;
		try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ObjectListing objectList = configuredS3Client().listObjects(bucketName);
			list = new ArrayList<String>();
			for (S3ObjectSummary summary : objectList.getObjectSummaries()) {
				list.add(summary.getKey());
			}
		} catch (Exception ex) {
			System.out.println("getS3BucketContentList() - Exception: " + ex.getMessage());
		}
		return list;
	}

	public String getS3BucketContent(String bucketName, String objectKey) {
		String content = null;
		try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ValidationUtility.validateKeyExists(objectKey, "bucket-object");
			S3Object s3Object = configuredS3Client().getObject(bucketName, objectKey);
			content = InputStreamUtility.getAsString(s3Object.getObjectContent());
		} catch (Exception ex) {
			System.out.println("getS3BucketContent() - Exception: " + ex.getMessage());
		}
		return content;
	}
    
    public void putS3BucketContent(String bucketName, String objectKey, String content) {
    	try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ValidationUtility.validateKeyExists(objectKey, "bucket-object");
			ValidationUtility.validateKeyExists(content, "content");
			configuredS3Client().putObject(bucketName, objectKey, content);
		} catch (Exception ex) {
			System.out.println("putS3BucketContent() - Exception: " + ex.getMessage());
    	}
    }
    
    public void postS3BucketContent(String bucketName, String objectKey, String content) {
    	try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ValidationUtility.validateKeyExists(objectKey, "bucket-object");
			validateS3ObjectDoesNotExist(bucketName, objectKey);
			putS3BucketContent(bucketName, objectKey, content);
		} catch (Exception ex) {
			System.out.println("postS3BucketContent() - Exception: " + ex.getMessage());
    	}
    };

	private AmazonS3 configuredS3Client() {
		AmazonS3 s3Client = null;
		try {
			AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
			builder.withCredentials(credentialsService.generateAwsCredentialProvider());
			builder.withRegion(getRegion());
			s3Client = builder.build();
		} catch (Exception ex) {
			System.out.println("configuredS3Client() - Exception: " + ex.getMessage());
    	}
		return s3Client;
	}

    private void validateS3ObjectDoesNotExist(String bucketName, String objectKey) throws Exception {
    	if (configuredS3Client().doesObjectExist(bucketName, objectKey)) {
    		throw new Exception("Object Key already exists");
    	}
    }

	private Regions getRegion() throws MissingAwsInformation {
		Regions awsRegion = Regions.valueOf(region);
		ValidationUtility.validateAwsRegion(awsRegion);
		return awsRegion;
	}
}
