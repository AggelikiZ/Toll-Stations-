package com.payway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("observatoryAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY) // Use API Key type
                                .in(SecurityScheme.In.HEADER) // Header-based security
                                .name("X-OBSERVATORY-AUTH") // Custom header name
                        ))
                .addSecurityItem(new SecurityRequirement().addList("observatoryAuth")); // Apply globally
    }
}

