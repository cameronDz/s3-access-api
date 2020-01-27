package org.md.s3accessapi.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.util.StringUtils;

public class InputStreamUtility {

	public static String getAsString(InputStream is) throws IOException {
		String response = null;
	    try {
	    	InputStreamReader streamReader = new InputStreamReader(is, StringUtils.UTF8);
		    StringBuilder sb = new StringBuilder();
	        BufferedReader reader = new BufferedReader(streamReader);
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        response = sb.toString();
	    } catch (Exception e) {
	    	// TODO catch
	    } finally {
	        is.close();
	    }
	    return response;
	}
}
