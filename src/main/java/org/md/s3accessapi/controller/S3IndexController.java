package org.md.s3accessapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@RequestMapping(path="/list", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBuckets() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			List<String> list = s3ClientService.getS3BucketContentList(bucketName);
			payload.put("payload", list);
			status = HttpStatus.OK;
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
			String objectKey = "index.json";
			String content = s3ClientService.getS3BucketContent(bucketName, objectKey);
			JsonNode node = new ObjectMapper().readTree(content);
			payload.put("payload", node);
			status = HttpStatus.OK;
		} catch (Exception ex) {
			System.out.println("getBucketContent() - Exception: " + ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
	
	@RequestMapping(path="/json/{key}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBucketJsonContent(@PathVariable String key) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			String content = s3ClientService.getS3BucketContent(bucketName, key + ".json");
			JsonNode node = new ObjectMapper().readTree(content);
			payload.put("payload", node);
			status = HttpStatus.OK;
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
		boolean successfullyIndexed = false;
		try {
			String objectKey = DateUtility.getCurrentDateTimeStampString() + ".json";
			String content = String.valueOf(body);
			s3ClientService.postS3BucketContent(bucketName, objectKey, content);
			status = HttpStatus.CREATED;
			payload.put("key", objectKey);
			if (index == true) {
				successfullyIndexed = s3ClientService.addKeyToJsonIndex(bucketName, objectKey);
			}
			payload.put("indexed", successfullyIndexed);
		} catch (Exception ex) {
			System.out.println("uploadBucketJsonContent() Exception: " + ex.getMessage());
		}
		System.out.println("map: " + payload);
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
}
