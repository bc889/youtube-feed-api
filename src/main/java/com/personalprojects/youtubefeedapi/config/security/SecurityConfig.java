package com.personalprojects.youtubefeedapi.config.security;

import com.personalprojects.youtubefeedapi.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationErrorHandler authenticationErrorHandler;
    private final ApplicationProperties applicationProperties;

    @Bean
    public SecurityFilterChain httpSecurity(final HttpSecurity http) throws Exception {
        if (applicationProperties.isDisableAuth()) {
            // Note: this project is not intended for use in production environments.
            return http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(AbstractHttpConfigurer::disable)
                    .oauth2ResourceServer(AbstractHttpConfigurer::disable)
                    .build();
        }
        return http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Endpoints the pubsub server & docs will use need to remain available.
                        .requestMatchers(
                                "/users/*/subscriptions/*/push",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/error").permitAll()
                        // Only users can access their own subs
                        .requestMatchers(
                                "/users/{userId}/notification-settings",
                                "/users/{userId}/subscriptions/**",
                                "/users/{userId}/feed/**"
                        )
                                .access(new WebExpressionAuthorizationManager(
                                        "#userId == authentication.principal.claims['sub']"
                                ))
                        // Read privileges for admin endpoint
                        .requestMatchers(HttpMethod.GET, "/admin/users/{userId}/subscriptions/**")
                            .hasAuthority("read:admin-all-user-subs")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/users/*/subscriptions/*/push")
                        .csrfTokenRepository(csrfTokenRepository()))
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(makePermissionsConverter()))
                        .authenticationEntryPoint(authenticationErrorHandler))
                .build();
    }

    private JwtAuthenticationConverter makePermissionsConverter() {
        final var jwtAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthoritiesConverter.setAuthoritiesClaimName("permissions");
        jwtAuthoritiesConverter.setAuthorityPrefix("");

        final var jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtAuthoritiesConverter);

        return jwtAuthConverter;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }
}