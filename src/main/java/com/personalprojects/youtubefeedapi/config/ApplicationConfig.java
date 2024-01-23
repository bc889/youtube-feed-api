package com.personalprojects.youtubefeedapi.config;

import com.personalprojects.youtubefeedapi.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Adds our Client Origin Url to our CORS policy.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig implements WebMvcConfigurer {

    private final ApplicationProperties applicationProps;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(applicationProps.getClientOriginUrl())
                .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, "ngrok-skip-browser-warning")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name()
                )
                .maxAge(86400);
    }
}