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

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.firebase")
public class FirebaseConfig {
    // Field retrieved from application.properties
    private String CREDENTIALS_FILE_PATH;

    @PostConstruct
    public void initialize() throws IOException {
        InputStream inputStream = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();

        FirebaseApp.initializeApp(options);
    }
}