package com.example.foody.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

/**
 * Configuration class for integrating Google Drive API.
 * This class provides the necessary beans and configuration to use Google Drive within the application.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.google-drive")
public class GoogleDriveConfig {

    /**
     * Application name, retrieved from application.properties.
     */
    @Value("${spring.application.name}")
    private String APPLICATION_NAME;

    /**
     * Path to the credentials file for Google Drive API, retrieved from application.properties.
     */
    private String CREDENTIALS_FILE_PATH;

    /**
     * Map of folder configurations, retrieved from application.properties.
     */
    private Map<String, String> FOLDERS;

    /**
     * Bean to configure and provide a {@link Drive} instance for interacting with Google Drive.
     *
     * @return A configured {@link Drive} instance.
     * @throws IOException              If there is an issue reading the credentials file.
     * @throws GeneralSecurityException If there is a security configuration issue.
     */
    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        InputStream inputStream = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        return new Drive.Builder(transport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}