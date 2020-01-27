package org.md.s3accessapi.controller;

import java.util.Arrays;
import java.util.List;

import org.md.s3accessapi.service.S3ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3IndexController {
	
	private S3ClientService s3ClientService = new S3ClientService();

	@RequestMapping("/test-list")
	public ResponseEntity<List<Integer>> getIndex() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<Integer> list = null;
		try {
			list = Arrays.asList(new Integer[]{ 1234, 325 });
			status = HttpStatus.OK;
		} catch (Exception ex) {
			// TODO catch
		}
		return new ResponseEntity<List<Integer>>(list, status);
	}
	
	@RequestMapping("/bucket-list")
	public ResponseEntity<List<String>> getBuckets() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<String> list = null;
		try {
			String bucketName = "";
			list = s3ClientService.getS3BucketContent(bucketName);
			status = HttpStatus.OK;
		} catch (Exception ex) {
			// TODO catch
		}
		return new ResponseEntity<List<String>>(list, status);
	}
}
