package com.apps.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "application.security")
public class ApplicationSecurityProperties {

    private String[] permitUrls;

    private String[] allowOrigins;

    private String[] corsExposedHeaders;
}