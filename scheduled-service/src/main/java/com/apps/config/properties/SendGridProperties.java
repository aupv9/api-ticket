package com.apps.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "sendgrid.mail"
)
@Data
public class SendGridProperties {
    private String key;
}
