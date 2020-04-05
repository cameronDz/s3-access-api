package org.md.api.s3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    
    private final String BASE_PACKAGES = "org.springframework.boot";
    
    private final String TITLE = "S3 Bucket REST API";
    private final String DESCRIPTION = "Set of end points used for CRUD operations against an AWS S3 bucket holding JSON objects.";
    private final String LICENSE = "Apache 2.0";
    private final String LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0.html";
    private final String VERSION = "1.0.0";
    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(Predicates.not(RequestHandlerSelectors.basePackage(BASE_PACKAGES)))            
          .paths(PathSelectors.any())     
          .build().apiInfo(apiEndPointsInfo());
    } 
    
    private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder()
			.title(TITLE)
			.description(DESCRIPTION)
			.license(LICENSE)
			.licenseUrl(LICENSE_URL)
			.version(VERSION)
			.build();
	}
}
