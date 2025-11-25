package com.ktotopawel.opsguard.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        String securityScheme = "userIdHeader";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securityScheme))
                .components(new Components()
                        .addSecuritySchemes(securityScheme, new SecurityScheme()
                                .name("X-User-Id")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)));
    }
}
