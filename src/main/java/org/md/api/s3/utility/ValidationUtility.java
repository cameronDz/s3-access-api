package org.md.api.s3.utility;

import org.md.api.s3.model.exception.MissingAwsInformation;
import org.md.api.s3.model.exception.MissingKeyException;

import com.amazonaws.regions.Regions;

/**
 * utility class used to throw exceptions when 
 * @author cameron
 */
public class ValidationUtility {

	/**
	 * check a key name exists, throws exception if it does not
	 * @param key value
	 * @param name of key
	 * @throws MissingKeyException
	 */
	public static void validateKeyExists(String keyValue, String keyName) throws MissingKeyException {
		if (isStringNullOrEmpty(keyValue)) {
			String message = "Invalid key: " + keyName;
			throw new MissingKeyException(message);
		}
	}

	public static void validateAwsRegion(Regions region) throws MissingAwsInformation {
		if (region == null) {
			throw new MissingAwsInformation("Provided Region is null.");
		}
	}

	private static boolean isStringNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

}
