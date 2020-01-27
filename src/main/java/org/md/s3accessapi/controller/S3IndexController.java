package org.md.s3accessapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.md.s3accessapi.service.S3ClientService;
import org.md.s3accessapi.utility.DateUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class S3IndexController {

	private S3ClientService s3ClientService = new S3ClientService();
	private String bucketName = null;

	@RequestMapping("/list")
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
	
	@RequestMapping("/index")
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
	
	@RequestMapping("/json")
	public ResponseEntity<Map<String, Object>> getBucketJsonContent() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			String objectKey = null;
			String content = s3ClientService.getS3BucketContent(bucketName, objectKey);
			JsonNode node = new ObjectMapper().readTree(content);
			payload.put("payload", node);
			status = HttpStatus.OK;
		} catch (Exception ex) {
			System.out.println("getBucketJsonContent() - Exception: " + ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}

	@RequestMapping("/upload")
	public ResponseEntity<Map<String, Object>> uploadBucketJsonContent() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			String objectKey = DateUtility.getCurrentDateTimeStampString() + ".json";
			String content = null;
			s3ClientService.postS3BucketContent(bucketName, objectKey, content);
			status = HttpStatus.OK;
			payload.put("payload", new ObjectMapper().readTree(content));
		} catch (Exception ex) {
			System.out.println("uploadBucketJsonContent() Exception: " + ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
}
