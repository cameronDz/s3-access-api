package org.md.s3accessapi.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3IndexController {

	@RequestMapping("/index")
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
}
