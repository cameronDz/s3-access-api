package org.md.api.s3.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationFilter implements Filter {

    private static final String EMPTY_STRING = "";

    @Value("${api.authentication.key}")
    private String authKey;

    @Value("${api.authentication.method}")
    private String authMethod;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        boolean isValidKey = true;
        if ((requiresAuthKey()) && (request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            String requestApiKey = getRequestApiKey((HttpServletRequest) request);
            if (!authKey.equals(requestApiKey)) {
                isValidKey = false;
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("Invalid API Key");
            }
        }
        if (isValidKey) {
            chain.doFilter(request, response);
        }
    }

    private boolean requiresAuthKey() {
        return (isNotNullOrEmptyString(authKey)) && (isNotNullOrEmptyString(authMethod));
    }

    private boolean isNotNullOrEmptyString(Object value) {
        return ((value != null) && (!EMPTY_STRING.equals(value)));
    }

    private String getRequestApiKey(HttpServletRequest httpRequest) {
        String apiKey = null;
        String authHeader = httpRequest.getHeader("Authorization");
        if(authHeader != null) {
            authHeader = authHeader.trim();
            if(authHeader.startsWith(authMethod + " ")) {
                apiKey = authHeader.substring(authMethod.length()).trim();
            }
        }
        return apiKey;
    }
}
