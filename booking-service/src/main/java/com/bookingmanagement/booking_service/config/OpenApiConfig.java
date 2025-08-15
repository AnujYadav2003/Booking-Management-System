package com.bookingmanagement.booking_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenApi(){
        return new OpenAPI()
                .info(new Info()

                        .description("Documentation")
                        .title("Swagger implementation for Booking Service")
                        .version("1.0.0")
                );
    }
}

