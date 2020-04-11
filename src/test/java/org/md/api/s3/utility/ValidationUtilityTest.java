package org.md.api.s3.utility;

import org.junit.jupiter.api.Test;
import org.md.api.s3.model.exception.MissingKeyException;

class ValidationUtilityTest {

    @Test
    void validateKeyExists_hasAllRequiredParams_noExceptionThrown() throws MissingKeyException {
        String keyValue = "keyValue";
        String keyName = "keyName";
        ValidationUtility.validateKeyExists(keyValue, keyName);
    }

}
