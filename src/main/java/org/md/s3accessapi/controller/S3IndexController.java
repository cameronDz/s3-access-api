package org.md.s3accessapi.controller;

import java.util.Date;
import java.util.List;

import org.md.s3accessapi.service.S3ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3IndexController {

	private S3ClientService s3ClientService = new S3ClientService();
	private String bucketName = "";

	@RequestMapping("/bucket-list")
	public ResponseEntity<List<String>> getBuckets() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<String> list = null;
		try {
			list = s3ClientService.getS3BucketContentList(bucketName);
			status = HttpStatus.OK;
		} catch (Exception ex) {
			// TODO catch
		}
		return new ResponseEntity<List<String>>(list, status);
	}
	
	@RequestMapping("/index")
	public ResponseEntity<String> getBucketContent() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String content = null;
		try {
			String objectKey = "index.json";
			content = s3ClientService.getS3BucketContent(bucketName, objectKey);
			status = HttpStatus.OK;
		} catch (Exception ex) {
			// TODO catch
		}
		return new ResponseEntity<String>(content, status);
	}
	
	@RequestMapping("/json")
	public ResponseEntity<String> getBucketJsonContent() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String content = null;
		try {
			String objectKey = "";
			content = s3ClientService.getS3BucketContent(bucketName, objectKey);
			status = HttpStatus.OK;
		} catch (Exception ex) {
			// TODO catch
		}
		return new ResponseEntity<String>(content, status);
	}
	
	@RequestMapping("/upload-test")
	public ResponseEntity<String> pubBucketJsonContent() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String content = null;
		try {
			String objectKey = "test-" + new Date().toString().replaceAll(" ", "") + ".json";
			content = "{ \"test\": { \"innner\": \"testing\" } }";
			s3ClientService.postS3BucketContent(bucketName, objectKey, content);
			status = HttpStatus.OK;
		} catch (Exception ex) {
			// TODO catch
			content = null;
		}
		return new ResponseEntity<String>(content, status);
	}
}
