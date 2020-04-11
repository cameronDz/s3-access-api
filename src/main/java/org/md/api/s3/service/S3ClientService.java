package org.md.api.s3.service;

import java.util.ArrayList;
import java.util.List;

import org.md.api.s3.model.exception.MissingAwsInformation;
import org.md.api.s3.model.exception.MissingKeyException;
import org.md.api.s3.utility.InputStreamUtility;
import org.md.api.s3.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@SpringBootConfiguration
public class S3ClientService {

	@Value("${s3.bucket.region}")
	private String region;

	@Autowired
	private AwsCredentialService credentialsService;

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
    
    public Boolean putS3BucketContent(String bucketName, String objectKey, String content) throws MissingAwsInformation, MissingKeyException {
        Boolean wasSuccessful = false;
    	try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ValidationUtility.validateKeyExists(objectKey, "bucket-object");
			ValidationUtility.validateKeyExists(content, "content");
			configuredS3Client().putObject(bucketName, objectKey, content);
			try {
			    configuredS3Client().setObjectAcl(bucketName, objectKey, CannedAccessControlList.PublicRead);
	        } catch (Exception e) {
	            System.out.println("ERROR: " + e.getMessage());
	        }
			wasSuccessful = true;
        } catch (MissingKeyException e) {
               throw e;
        } catch (Exception e) {
		    throw new MissingAwsInformation(e.getMessage());
    	}
    	return wasSuccessful;
    }

	public Boolean updateS3BucketIndex(String bucketName, String key) throws MissingAwsInformation, MissingKeyException {
		Boolean updated = null;
		String bucketObject = "index.json";
		try {
			boolean found = false;
			ArrayNode content = (ArrayNode) new ObjectMapper().readTree(getS3BucketContent(bucketName, bucketObject)).get("list");
			Integer length = content.size();
			for (int index = 0; index < length; index++) {
				if (key != null && key.equals(content.get(index).asText())) {
					content.remove(index);
					found = true;
					break;
				}
			}
			if (found == true) {
				String newContent = "{ \"list\": " + new ObjectMapper().writeValueAsString(content) + " }";
				putS3BucketContent(bucketName, bucketObject, newContent);
				updated = true;
			} else {
				updated = false;
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return updated;
	}
    
    public boolean addKeyToJsonIndex(String bucketName, String keyName) {
    	boolean keyAddedToIndex = false;
    	try {
    		String indexString = getS3BucketContent(bucketName, "index.json");
    		ArrayNode listNode = (ArrayNode) new ObjectMapper().readTree(indexString).get("list");
    		listNode.add(Long.valueOf(keyName.replace(".json", "")));
    		String content = "{\"list\":" + String.valueOf(listNode) + "}";
    		putS3BucketContent(bucketName, "index.json", content);
    		keyAddedToIndex = true;
    	} catch (Exception ex) {
			System.out.println("addKeyToJsonIndex() - Exception: " + ex.getMessage());
    	}
    	return keyAddedToIndex;
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
		Regions awsRegion = Regions.fromName(region);
		ValidationUtility.validateAwsRegion(awsRegion);
		return awsRegion;
	}
}
