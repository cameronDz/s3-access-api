package org.md.s3accessapi.service;

import org.md.s3accessapi.model.exception.FeatureFlagException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class FeatureFlagService {

	@Value("${feature.flag.enable.delete}")
	private boolean isDeleteEnabled;
	
	@Value("${feature.flag.enable.get}")
	private boolean isGetEnabled;

	@Value("${feature.flag.enable.post}")
	private boolean isPostEnabled;

	@Value("${feature.flag.enable.put}")
	private boolean isPutEnabled;

	public FeatureFlagService() {
		super();
	}
	
	public void httpRequestFlagIsEnabled(String method) throws FeatureFlagException {
		if (method == null) {
			throw new FeatureFlagException("Null method name");
		}
		switch(method) {
		case ("DELETE"):
			isMethodEnabled(method, isDeleteEnabled);
			break;
		case ("GET"): 
			isMethodEnabled(method, isGetEnabled);
			break;
		case ("POST"): 
			isMethodEnabled(method, isPostEnabled);
			break;
		case ("PUT"): 
			isMethodEnabled(method, isPutEnabled);
			break;
		default:
			throw new FeatureFlagException("Unknown method name");
		}
	}
	
	private void isMethodEnabled(String name, boolean isEnabled) throws FeatureFlagException {
		if (isEnabled != true) {
			throw new FeatureFlagException("Method not enabled: " + name);
		}
	}
}
