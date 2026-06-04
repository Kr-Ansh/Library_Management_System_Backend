package com.irons.library_management_system_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LibraryManagementSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementSystemBackendApplication.class, args);
    }

}
