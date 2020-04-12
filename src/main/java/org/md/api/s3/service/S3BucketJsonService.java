package org.md.api.s3.service;

import java.util.List;

import org.md.api.s3.model.exception.AwsS3GeneralException;
import org.md.api.s3.utility.DateUtility;
import org.md.api.s3.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class S3BucketJsonService {

    private final String JSON_EXTENSION = ".json";

    @Autowired
    private S3ClientService s3ClientService;

    /**
     * get a list of all the .JSON objects in a AWS S3 bucket
     * @param bucketName name of bucket to get list of objects
     * @return list of strings of files names on bucket
     * @throws AwsS3GeneralException
     */
    public List<String> getS3BucketJsonContentList(String bucketName) throws AwsS3GeneralException {
        List<String> list = s3ClientService.getS3BucketContentList(bucketName);
        int listSize = list != null ? list.size() : -1;
        for (int i = listSize-1; i >= 0; i--) {
            if (!list.get(i).endsWith(JSON_EXTENSION)) {
                list.remove(i);
            }
        }
        return list;
    }

    /**
     * get a specific JSON file from AWS S3 bucket
     * @param bucketName name of bucket to get list of objects
     * @param objectKey name of JSON object to fetch (DO NOT INCLUDE EXTENSION)
     * @return string of file content
     * @throws AwsS3GeneralException
     */
    public String getS3BucketJsonContent(String bucketName, String objectKey) throws AwsS3GeneralException {
        String key = objectKey + JSON_EXTENSION;
        return s3ClientService.getS3BucketContent(bucketName, key);
    }

    /**
     * Upload a new JSON object key to AWS S3 bucket
     * @param bucketName name of S3 bucket
     * @param objectKey name of object key; if null, will default to current DateTime Long value
     * @param content string of file content for JSON object
     * @return name of the object key uploaded to S3 if successful
     * @throws AwsS3GeneralException
     */
    public String postS3BucketJsonContent(String bucketName, String objectKey, String content) throws AwsS3GeneralException {
        String objectKeyName = ValidationUtility.isStringNullOrEmpty(objectKey) ? DateUtility.getCurrentDateTimeStampString() : objectKey;
        String key = objectKeyName + JSON_EXTENSION;
        s3ClientService.postS3BucketContent(bucketName, key, content);
        return objectKeyName;
    }

    /**
     * Update an existing JSON object key in an AWS S3 bucket
     * @param bucketName name of S3 bucket
     * @param objectKey name of object key
     * @param content string of file content for JSON object
     * @return true if able to update object, false otherwise
     * @throws AwsS3GeneralException
     */
    public Boolean putS3BucketJsonContent(String bucketName, String objectKey, String content) throws AwsS3GeneralException {
        String key = objectKey + JSON_EXTENSION;
        return s3ClientService.postS3BucketContent(bucketName, key, content);
    }
}
