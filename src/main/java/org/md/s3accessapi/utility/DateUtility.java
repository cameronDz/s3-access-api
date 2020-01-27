package org.md.s3accessapi.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {

	public static String getCurrentDateTimeStampString() {
		DateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		return dateFormat.format(new Date());
	}
}
