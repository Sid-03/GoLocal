package com.golocal; // Verify this matches your directory structure under src/main/java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; // Ensure this import is present

/**
 * Main application entry point class for the Inquiry Service.
 * The @SpringBootApplication annotation enables auto-configuration,
 * component scanning, and other Spring Boot features.
 */
@SpringBootApplication // This annotation is crucial for Spring Boot to identify the main class
public class InquiryServiceApplication { // Ensure class name matches the file name

    /**
     * The main method which serves as the entry point for the Java application.
     * It delegates to Spring Boot's SpringApplication class to launch the application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) { // Ensure method signature is correct
        // Launches the Spring Boot application
        SpringApplication.run(InquiryServiceApplication.class, args);

        // Optional: Print a confirmation message to the console on startup
        System.out.println("\n--- Inquiry Service Started Successfully ---\n");
    }

}