package com.personalprojects.youtubefeedapi.config.properties;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    String clientOriginUrl;
    boolean disableScheduler;
    boolean disableAuth;
}

