package org.md.s3accessapi.utility;

import org.md.s3accessapi.model.exception.MissingKeyException;

/**
 * utility class used to throw exceptions when 
 * @author cameron
 *
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

	private static boolean isStringNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
}
