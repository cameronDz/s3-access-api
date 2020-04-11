package org.md.api.s3.utility;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateUtilityTest {

    @Test
    void getCurrentDateTimeStampString_passValidDate_returnString() {
        String formattedDate = DateUtility.getCurrentDateTimeStampString();
        int expected = 17;
        int actual = formattedDate.length();
        assertEquals(expected, actual);
    }

}
