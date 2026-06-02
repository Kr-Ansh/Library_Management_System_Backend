package com.irons.library_management_system_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management System Backend")
                        .description("""
                                ### RESTful Backend Engine for Library Inventory Management
                                
                                This API serves as the core operational layer for managing a library ecosystem. 
                                It handles complex data flows including real-time inventory tracking, bidirectional 
                                book-to-patron borrowing transactions, strict payload input validation, and centralized 
                                error handling mechanisms.
                                
                                **Key Features Engineered:**
                                * **Books Engine:** Complete inventory control, filtering by availability, genre, and author tracking.
                                * **Users Portal:** Management of members and admin access controls.
                                * **Transaction Pipeline:** Atomic patching operations for secure borrowing and return workflows.
                                """)
                        .version("1.0")
                );
    }
}
