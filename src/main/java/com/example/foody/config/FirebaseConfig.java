package com.example.foody.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Configuration class for Firebase integration.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.firebase")
public class FirebaseConfig {

    /**
     * Path to the Firebase credentials file, retrieved from application.properties.
     */
    private String CREDENTIALS_FILE_PATH;

    /**
     * Initializes the Firebase application with the provided credentials.
     *
     * @throws IOException if an error occurs while reading the credentials file
     */
    @PostConstruct
    public void initialize() throws IOException {
        InputStream inputStream = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();

        FirebaseApp.initializeApp(options);
    }
}