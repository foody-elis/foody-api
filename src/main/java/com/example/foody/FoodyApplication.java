package com.example.foody;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Entry point for the Foody application.
 * <p>
 * This class is annotated with {@link SpringBootApplication}, which marks it as the primary
 * Spring Boot application class, enabling auto-configuration and component scanning.
 * <p>
 * The {@link EnableAsync} annotation enables asynchronous processing, allowing the application
 * to execute tasks in a separate thread without blocking the main flow.
 */
@EnableAsync
@SpringBootApplication
public class FoodyApplication {

    /**
     * Main method to run the Foody application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FoodyApplication.class, args);
    }
}