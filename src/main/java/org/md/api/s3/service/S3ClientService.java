package org.md.api.s3.service;

import java.util.ArrayList;
import java.util.List;

import org.md.api.s3.model.exception.AwsS3GeneralException;
import org.md.api.s3.model.exception.MissingAwsInformation;
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

@SpringBootConfiguration
public class S3ClientService {
    
    private final String AWS_ACCESS_EXCEPTION_MESSAGE = "Could not access AWS S3 bucket using client: ";
    private final String AWS_CONFIGURATION_EXCEPTION_MESSAGE = "Could not configure AWS S3 Client for accessing Bucket: ";
    private final String AWS_OBJECT_EXISTS_EXCEPTION_MESSAGE = "Object Key already exists";

	@Value("${s3.bucket.region}")
	private String region;
	
	@Value("${s3.bucket.is.public")
	private boolean isBucketPublic;

	@Autowired
	private AwsCredentialService credentialsService;

	public S3ClientService() {
		super();
	}

	/**
	 * gets a list of all objects in AWS S3 bucket
	 * @param bucketName name of bucket to get list
	 * @return list of string of all object filenames in bucket
	 * @throws AwsS3GeneralException
	 */
    public List<String> getS3BucketContentList(String bucketName) throws AwsS3GeneralException {
		List<String> list = new ArrayList<String>();
		try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ObjectListing objectList = configuredS3Client().listObjects(bucketName);
			for (S3ObjectSummary summary : objectList.getObjectSummaries()) {
				list.add(summary.getKey());
			}
        } catch (AwsS3GeneralException awsException) {
            throw awsException;
        } catch (Exception ex) {
            throw new AwsS3GeneralException(AWS_ACCESS_EXCEPTION_MESSAGE + ex.getMessage());
        }
		return list;
	}

    /**
     * get a specific object on S3 bucket
     * @param bucketName name of bucket
     * @param objectKey name of object in bucket
     * @return string of content of object in bucket
     * @throws AwsS3GeneralException
     */
	public String getS3BucketContent(String bucketName, String objectKey) throws AwsS3GeneralException {
		String content = null;
		try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ValidationUtility.validateKeyExists(objectKey, "bucket-object");
			S3Object s3Object = configuredS3Client().getObject(bucketName, objectKey);
			content = InputStreamUtility.getAsString(s3Object.getObjectContent());
        } catch (AwsS3GeneralException awsException) {
            throw awsException;
        } catch (Exception ex) {
            throw new AwsS3GeneralException(AWS_ACCESS_EXCEPTION_MESSAGE + ex.getMessage());
        }
		return content;
	}
    
	/**
	 * create a new object in AWS S3 bucket
	 * @param bucketName name of bucket to put object
	 * @param objectKey name of object being put in bucket
	 * @param content content of file
	 * @return true if object was created
	 * @throws AwsS3GeneralException
	 */
    public Boolean postS3BucketContent(String bucketName, String objectKey, String content) throws AwsS3GeneralException {
        Boolean wasSuccessful = false;
        try {
            ValidationUtility.validateKeyExists(bucketName, "bucket");
            ValidationUtility.validateKeyExists(objectKey, "bucket-object");
            validateS3ObjectDoesNotExist(bucketName, objectKey);
            wasSuccessful = putS3BucketContent(bucketName, objectKey, content);
        } catch (AwsS3GeneralException awsException) {
            throw awsException;
        } catch (Exception ex) {
            throw new AwsS3GeneralException(AWS_ACCESS_EXCEPTION_MESSAGE + ex.getMessage());
        }
        return wasSuccessful;
    };
    
    /**
     * update an AWS S3 object
     * @param bucketName name of bucket where object is located
     * @param objectKey object name to be updated
     * @param content content to be put in object
     * @return true if object was update, false otherwise
     * @throws AwsS3GeneralException
     */
    public Boolean putS3BucketContent(String bucketName, String objectKey, String content) throws AwsS3GeneralException {
        Boolean wasSuccessful = false;
    	try {
			ValidationUtility.validateKeyExists(bucketName, "bucket");
			ValidationUtility.validateKeyExists(objectKey, "bucket-object");
			ValidationUtility.validateKeyExists(content, "content");
			configuredS3Client().putObject(bucketName, objectKey, content);
			CannedAccessControlList acl = isBucketPublic ? CannedAccessControlList.PublicRead : CannedAccessControlList.Private;
		    configuredS3Client().setObjectAcl(bucketName, objectKey, acl);
			wasSuccessful = true;
        } catch (AwsS3GeneralException awsException) {
            throw awsException;
        } catch (Exception ex) {
            throw new AwsS3GeneralException(AWS_ACCESS_EXCEPTION_MESSAGE + ex.getMessage());
        }
    	return wasSuccessful;
    }

    /**
     * get configured s3 client 
     * @return
     */
	private AmazonS3 configuredS3Client() throws AwsS3GeneralException {
		AmazonS3 s3Client = null;
		try {
			AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
			builder.withCredentials(credentialsService.generateAwsCredentialProvider());
			builder.withRegion(getRegion());
			s3Client = builder.build();
		} catch (AwsS3GeneralException awsException) {
            throw awsException;
        } catch (Exception ex) {
            String message = AWS_CONFIGURATION_EXCEPTION_MESSAGE + ex.getMessage();
            throw new AwsS3GeneralException(message);
        }
		return s3Client;
	}

	/**
	 * throws exception if bucket already has existing object (for POST requests)
	 * @param bucketName name of AWS S3 bucket
	 * @param objectKey name of object to be created
	 * @throws AwsS3GeneralException exception thrown if 
	 */
    private void validateS3ObjectDoesNotExist(String bucketName, String objectKey) throws AwsS3GeneralException {
    	if (configuredS3Client().doesObjectExist(bucketName, objectKey)) {
    		throw new AwsS3GeneralException(AWS_OBJECT_EXISTS_EXCEPTION_MESSAGE);
    	}
    }

    /**
     * get AWS S3 bucket region, throws exception if unable to get region
     * @return Region to be used for client configuration
     * @throws MissingAwsInformation exception thrown
     */
	private Regions getRegion() throws MissingAwsInformation {
		Regions awsRegion = Regions.fromName(region);
		ValidationUtility.validateAwsRegion(awsRegion);
		return awsRegion;
	}
}
