package com.ntt.tl.evaluation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@Import({OpenApiProperties.class})
public class OpenApiConfiguration {

    private OpenApiProperties openApiProperties;
    
    public OpenApiConfiguration(OpenApiProperties openApiProperties) {
        Assert.notNull(openApiProperties.getTitle(), "Missing title in openapi properties");
        Assert.notNull(openApiProperties.getDescription(), "Missing description in openapi properties");
        Assert.notNull(openApiProperties.getVersion(), "Missing version in openapi properties");
        this.openApiProperties = openApiProperties;
    }
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title(openApiProperties.getTitle())
                .description(openApiProperties.getDescription())
                .version(openApiProperties.getVersion())
                .license(new License()
                        .name(openApiProperties.getLicense())
                        .url(openApiProperties.getLicenseUrl()))
                .termsOfService(openApiProperties.getTermsOfService())
                .extensions(openApiProperties.getExtensions());
    }
    
}
