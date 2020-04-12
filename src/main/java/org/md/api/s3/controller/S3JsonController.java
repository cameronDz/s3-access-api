package org.md.api.s3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.md.api.s3.model.exception.FeatureFlagException;
import org.md.api.s3.service.FeatureFlagService;
import org.md.api.s3.service.S3BucketJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping(path="/json")
public class S3JsonController {

    private static final String NOT_IMPLEMENTED_EXCEPTION_MESSAGE = "Method not implemented";

    private static final String KEY_NAME_ERROR_MESSAGE = "errorMessage";
    private static final String KEY_NAME_NEW_OBJECT_NAME = "newObjectKeyName";
    private static final String KEY_NAME_PAYLOAD = "payload";
    private static final String KEY_NAME_UPDATED = "updated";

	@Value("${s3.bucket.name}")
	private String bucketName;

    @Autowired
    private S3BucketJsonService s3BucketJsonService;

	@Autowired
	private FeatureFlagService featureFlagService;

	@ApiOperation(value = "List of all JSON objects in S3 bucket")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list."),
	    @ApiResponse(code = 423, message = "Feature is currently locked."),
	    @ApiResponse(code = 500, message = "Some unexpected issue happened.")
	})
	@RequestMapping(path="/list", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBucketJsonList() {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			featureFlagService.httpRequestFlagIsEnabled(RequestMethod.GET.toString());
			List<String> list = s3BucketJsonService.getS3BucketJsonContentList(bucketName);
			payload.put(KEY_NAME_PAYLOAD, list);
			status = HttpStatus.OK;
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
            payload.put(KEY_NAME_ERROR_MESSAGE, ffEx.getMessage());
		} catch (Exception ex) {
            payload.put(KEY_NAME_ERROR_MESSAGE, ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}

    @ApiOperation(value = "Get specific JSON key from S3 bucket")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved key."),
        @ApiResponse(code = 423, message = "Feature is currently locked."),
        @ApiResponse(code = 500, message = "Some unexpected issue happened.")
    })
	@RequestMapping(path="/object/{key}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBucketJsonContent(
			@PathVariable String key) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			featureFlagService.httpRequestFlagIsEnabled(RequestMethod.GET.toString());
			String content = s3BucketJsonService.getS3BucketJsonContent(bucketName, key);
			payload.put(KEY_NAME_PAYLOAD, new ObjectMapper().readTree(content));
			status = HttpStatus.OK;
		} catch (FeatureFlagException ffEx) {
            status = HttpStatus.LOCKED;
            payload.put(KEY_NAME_ERROR_MESSAGE, ffEx.getMessage());
        } catch (Exception ex) {
            payload.put(KEY_NAME_ERROR_MESSAGE, ex.getMessage());
        }
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}

    @ApiOperation(value = "Upload a specific JSON key to S3 bucket; file will be named with Long value of current DateTime if no key is provided")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully uploaded key."),
        @ApiResponse(code = 423, message = "Feature is currently locked."),
        @ApiResponse(code = 500, message = "Some unexpected issue happened.")
    })
	@RequestMapping(path="/upload/{key}", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> uploadBucketJsonContent(
			@PathVariable(required=false) String key,
			@RequestBody JsonNode body) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			featureFlagService.httpRequestFlagIsEnabled(RequestMethod.POST.toString());
			String objectKey = s3BucketJsonService.postS3BucketJsonContent(bucketName, key, String.valueOf(body));
			status = HttpStatus.CREATED;
			payload.put(KEY_NAME_NEW_OBJECT_NAME, objectKey);
		} catch (FeatureFlagException ffEx) {
            status = HttpStatus.LOCKED;
            payload.put(KEY_NAME_ERROR_MESSAGE, ffEx.getMessage());
        } catch (Exception ex) {
            payload.put(KEY_NAME_ERROR_MESSAGE, ex.getMessage());
        }
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}

    @ApiOperation(value = "Update a specific JSON key to S3 bucket")
    @ApiResponses(value = {
        @ApiResponse(code = 423, message = "Feature is currently locked."),
        @ApiResponse(code = 500, message = "Some unexpected issue happened."),
        @ApiResponse(code = 501, message = "Feature is not implemented.")
    })
	@RequestMapping(path="/update/{key}", method=RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateBucketJsonContent(
			@RequestParam(required=false, defaultValue="false") Boolean index,
			@RequestBody JsonNode body,
			@PathVariable String key) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		Boolean successfullyUpdated = false;
		try {
			featureFlagService.httpRequestFlagIsEnabled(RequestMethod.PUT.toString());
			String content = new ObjectMapper().writeValueAsString(body);
			successfullyUpdated = s3BucketJsonService.putS3BucketJsonContent(bucketName, key, content);
			status = HttpStatus.OK;
		} catch (FeatureFlagException ffEx) {
			status = HttpStatus.LOCKED;
			payload.put(KEY_NAME_ERROR_MESSAGE, ffEx.getMessage());
		} catch (NotImplementedException niEx) {
			status = HttpStatus.NOT_IMPLEMENTED;
            payload.put(KEY_NAME_ERROR_MESSAGE, niEx.getMessage());
		} catch (Exception ex) {
            payload.put(KEY_NAME_ERROR_MESSAGE, ex.getMessage());
		}
		payload.put(KEY_NAME_UPDATED, successfullyUpdated);
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}

    @ApiOperation(value = "Delete a specific JSON key to S3 bucket")
    @ApiResponses(value = {
        @ApiResponse(code = 423, message = "Feature is currently locked."),
        @ApiResponse(code = 500, message = "Some unexpected issue happened."),
        @ApiResponse(code = 501, message = "Feature is not implemented.")
    })
	@RequestMapping(path="/delete/{key}", method=RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteBucketJsonObject(
			@PathVariable String key) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			featureFlagService.httpRequestFlagIsEnabled(RequestMethod.DELETE.toString());
			throw new NotImplementedException(NOT_IMPLEMENTED_EXCEPTION_MESSAGE);
        } catch (FeatureFlagException ffEx) {
            status = HttpStatus.LOCKED;
            payload.put(KEY_NAME_ERROR_MESSAGE, ffEx.getMessage());
        } catch (NotImplementedException niEx) {
            status = HttpStatus.NOT_IMPLEMENTED;
            payload.put(KEY_NAME_ERROR_MESSAGE, niEx.getMessage());
        } catch (Exception ex) {
            payload.put(KEY_NAME_ERROR_MESSAGE, ex.getMessage());
        }
		return new ResponseEntity<Map<String, Object>>(payload, status);
	}
}
