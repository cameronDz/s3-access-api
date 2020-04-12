package org.md.api.s3.utility;

import org.md.api.s3.model.exception.MissingAwsInformation;
import org.md.api.s3.model.exception.MissingKeyException;

import com.amazonaws.regions.Regions;

/**
 * utility class used to throw exceptions when
 * @author cameron
 */
public class ValidationUtility {

    private static final String MISSING_REGION_ERROR = "Provided Region is null.";

	/**
	 * check a key name exists, throws exception if it does not
	 * @param key value
	 * @param name of key
	 * @throws MissingKeyException thrown when no key value is provided
	 */
	public static void validateKeyExists(String keyValue, String keyName) throws MissingKeyException {
		if (isStringNullOrEmpty(keyValue)) {
			String message = "Invalid key: " + keyName;
			throw new MissingKeyException(message);
		}
	}

	/**
	 * check is AWS region is set correctly
	 * @param region AWS object used for holding region
	 * @throws MissingAwsInformation thrown when no region is provided
	 */
	public static void validateAwsRegion(Regions region) throws MissingAwsInformation {
		if (region == null) {
			throw new MissingAwsInformation(MISSING_REGION_ERROR);
		}
	}

	/**
	 * check if a string is null or empty
	 * @param s string being checked
	 * @return true if null or empty, false otherwise
	 */
	public static boolean isStringNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

}
