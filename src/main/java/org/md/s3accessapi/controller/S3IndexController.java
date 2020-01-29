package org.md.s3accessapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.md.s3accessapi.model.exception.FeatureFlagException;
import org.md.s3accessapi.service.FeatureFlagService;
import org.md.s3accessapi.service.S3ClientService;
import org.md.s3accessapi.utility.DateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class S3IndexController {

	@Value("${s3.bucket.name}")
	private String bucketName;

	@Autowired
	private S3ClientService s3ClientService;

	@Autowired
	private FeatureFlagService featureFlagService;

	@RequestMapping(path="/list", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBuckets() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			featureFlagService.httpRequestFlagIsEnabled("GET");
			List<String> list = s3ClientService.getS3BucketContentList(bucketName);
			payload.put("payload", list);
			status = HttpStatus.OK;
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
			System.out.println("getBuckets() Exception: " + ffEx.getMessage());
		} catch (Exception ex) {
			System.out.println("getBuckets() - Exception: " + ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
	
	@RequestMapping(path="/index", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBucketContent() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			featureFlagService.httpRequestFlagIsEnabled("GET");
			String objectKey = "index.json";
			String content = s3ClientService.getS3BucketContent(bucketName, objectKey);
			JsonNode node = new ObjectMapper().readTree(content);
			payload.put("payload", node);
			status = HttpStatus.OK;
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
			System.out.println("getBucketContent() Exception: " + ffEx.getMessage());
		} catch (Exception ex) {
			System.out.println("getBucketContent() - Exception: " + ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
	
	@RequestMapping(path="/json/{key}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBucketJsonContent(
			@PathVariable String key) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			featureFlagService.httpRequestFlagIsEnabled("GET");
			String content = s3ClientService.getS3BucketContent(bucketName, key + ".json");
			JsonNode node = new ObjectMapper().readTree(content);
			payload.put("payload", node);
			status = HttpStatus.OK;
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
			System.out.println("getBucketJsonContent() Exception: " + ffEx.getMessage());
		} catch (Exception ex) {
			System.out.println("getBucketJsonContent() - Exception: " + ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}

	@RequestMapping(path="/upload", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> uploadBucketJsonContent(
			@RequestParam(required=false, defaultValue="false") Boolean index,
			@RequestBody JsonNode body) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		Boolean successfullyIndexed = null;
		try {
			featureFlagService.httpRequestFlagIsEnabled("POST");
			String objectKey = DateUtility.getCurrentDateTimeStampString() + ".json";
			String content = String.valueOf(body);
			s3ClientService.postS3BucketContent(bucketName, objectKey, content);
			status = HttpStatus.CREATED;
			payload.put("key", objectKey);
			if (index == true) {
				successfullyIndexed = s3ClientService.addKeyToJsonIndex(bucketName, objectKey);
			}
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
			System.out.println("uploadBucketJsonContent() Exception: " + ffEx.getMessage());
		} catch (Exception ex) {
			System.out.println("uploadBucketJsonContent() Exception: " + ex.getMessage());
		}
		payload.put("indexed", successfullyIndexed);
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
	
	@RequestMapping(path="/update/{key}", method=RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateBucketJsonContent(
			@RequestParam(required=false, defaultValue="false") Boolean index,
			@RequestBody JsonNode body,
			@PathVariable String key) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		Boolean successfullyIndexed = null;
		try {
			featureFlagService.httpRequestFlagIsEnabled("PUT");
			throw new NotImplementedException("Method not implemented");
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
			System.out.println("uploadBucketJsonContent() Exception: " + ffEx.getMessage());
		} catch (Exception ex) {
			System.out.println("uploadBucketJsonContent() Exception: " + ex.getMessage());
		}
		payload.put("indexed", successfullyIndexed);
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
	
	@RequestMapping(path="/delete/{key}", method=RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteBucketJsonObject(
			@PathVariable String key) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		Boolean removedFromIndexed = null;
		try {
			featureFlagService.httpRequestFlagIsEnabled("DELETE");
			throw new NotImplementedException("Method not implemented");
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
			System.out.println("uploadBucketJsonContent() Exception: " + ffEx.getMessage());
		} catch (Exception ex) {
			System.out.println("uploadBucketJsonContent() Exception: " + ex.getMessage());
		}
		payload.put("indexed", removedFromIndexed);
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
}
