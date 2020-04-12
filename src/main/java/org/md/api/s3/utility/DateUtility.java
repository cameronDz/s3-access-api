package org.md.api.s3.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {

    private static final String DATE_FORMAT = "YYYYMMddHHmmssSSS";

    /**
     * format date in the following format; March 15, 2020 9:01PM -> 20200315210100000
     * @param date date to be formated; null dates use current date
     * @return string of formatted date
     */
    public static String getCurrentDateTimeStampString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return  date == null ? dateFormat.format(new Date()) : dateFormat.format(date);
    }

    /**
     * format current date value in following way; March 15, 2020 9:01PM -> 20200315210100000
     * @return formatted date string
     */
	public static String getCurrentDateTimeStampString() {
	    return getCurrentDateTimeStampString(new Date());
	}
}
