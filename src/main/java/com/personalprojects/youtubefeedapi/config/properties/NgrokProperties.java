package com.personalprojects.youtubefeedapi.config.properties;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ngrok")
public class NgrokProperties {
    String baseUrl;
}
