package org.md.api.s3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@SpringBootConfiguration
public class TokenValidationService {

    private static final String ERROR_JSON = "Unable to read token";
    private static final String ERROR_TOKEN = "Invalid API Key";
    private static final String TOKEN = "token";

    @Value("${api.jwt.verify.endpoint}")
    private String jwtVerifyEndpoint;

    public void validateToken(HttpHeaders headers) {
        try {
            if (jwtVerifyEndpoint != null && !"".equals(jwtVerifyEndpoint)) {
                String authHeaderKey = "Authorization";
                String authBearer = "Bearer";
                String body;
                try {
                    body = new ObjectMapper().writeValueAsString(new HashMap<String, String>() {{
                        put(TOKEN, headers.get(authHeaderKey).get(0).substring(authBearer.length()).trim());
                    }});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(ERROR_JSON);
                }

                HttpHeaders requestHeader = new HttpHeaders();
                requestHeader.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<String>(body, requestHeader);
                ResponseEntity<String> result = new RestTemplate().exchange(
                    jwtVerifyEndpoint,
                    HttpMethod.POST,
                    entity,
                    String.class);
                if (result.getStatusCode() != HttpStatus.OK) {
                    throw new RuntimeException(ERROR_TOKEN);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
