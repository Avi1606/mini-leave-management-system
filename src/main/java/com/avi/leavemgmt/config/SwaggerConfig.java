package com.avi.leavemgmt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mini Leave Management System API")
                        .description("A comprehensive REST API for managing employee leave requests")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Avi")
                                .email("avi@example.com")
                                .url("https://github.com/Avi1606"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}