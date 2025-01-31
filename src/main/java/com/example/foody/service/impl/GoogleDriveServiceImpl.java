package com.example.foody.service.impl;

import com.example.foody.config.GoogleDriveConfig;
import com.example.foody.exceptions.google_drive.GoogleDriveFileDeleteException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.utils.enums.GoogleDriveFileType;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Implementation of the {@link GoogleDriveService} interface.
 * <p>
 * Provides methods to upload and delete images on Google Drive.
 */
@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private static final String GOOGLE_DRIVE_URL = "https://drive.google.com/uc?id=";
    private static final String IMAGE_MIME_TYPE = "image/jpeg";
    private static final String IMAGE_EXTENSION = "jpeg";
    private final Drive drive;
    private final Map<String, String> folders;

    /**
     * Constructs an instance of {@link GoogleDriveServiceImpl}.
     *
     * @param drive             the Google Drive service to use for file operations
     * @param googleDriveConfig the configuration for Google Drive folders
     */
    public GoogleDriveServiceImpl(
            Drive drive,
            GoogleDriveConfig googleDriveConfig) {
        this.drive = drive;
        this.folders = googleDriveConfig.getFOLDERS();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method uploads a base64 encoded image to Google Drive.
     *
     * @param base64Image the base64 encoded image
     * @param fileType    the type of the file to upload
     * @return the public URL of the uploaded image
     * @throws GoogleDriveFileUploadException if there is an error during the upload
     */
    @Override
    public String uploadBase64Image(String base64Image, GoogleDriveFileType fileType) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            String folderId = getFolderId(fileType);
            String fileId = uploadImage(decodedBytes, fileType.name().toLowerCase(), folderId);
            return generatePublicLink(fileId);
        } catch (Exception e) {
            throw new GoogleDriveFileUploadException(fileType);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method deletes an image from Google Drive.
     *
     * @param fileUrl the URL of the file to delete
     * @throws GoogleDriveFileDeleteException if there is an error during the deletion
     */
    @Override
    public void deleteImage(String fileUrl) {
        try {
            String fileId = fileUrl.substring(GOOGLE_DRIVE_URL.length());
            drive.files().delete(fileId).execute();
        } catch (Exception e) {
            throw new GoogleDriveFileDeleteException(fileUrl);
        }
    }

    /**
     * Retrieves the folder ID for the given file type.
     *
     * @param fileType the type of the file
     * @return the folder ID
     * @throws IllegalArgumentException if the folder ID is not found for the file type
     */
    private String getFolderId(GoogleDriveFileType fileType) {
        String folderId = folders.get(fileType.getFolderIdKey());
        if (folderId == null) {
            throw new IllegalArgumentException("Folder ID not found for file type: " + fileType);
        }

        return folderId;
    }

    /**
     * Uploads the image to Google Drive.
     *
     * @param image    the image bytes
     * @param type     the type of the file
     * @param folderId the folder ID to upload the file to
     * @return the ID of the uploaded file
     * @throws IOException if there is an error during the upload
     */
    private String uploadImage(byte[] image, String type, String folderId) throws IOException {
        File file = createFileMetadata(type, folderId);
        com.google.api.client.http.InputStreamContent mediaContent =
                new com.google.api.client.http.InputStreamContent(IMAGE_MIME_TYPE, new ByteArrayInputStream(image));

        File uploadedFile = drive.files().create(file, mediaContent)
                .setFields("id")
                .execute();

        return uploadedFile.getId();
    }

    /**
     * Generates a public link for the uploaded file.
     *
     * @param fileId the ID of the uploaded file
     * @return the public URL of the file
     * @throws IOException if there is an error during the permission setting
     */
    private String generatePublicLink(String fileId) throws IOException {
        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");
        drive.permissions().create(fileId, permission).execute();

        return GOOGLE_DRIVE_URL + fileId;
    }

    /**
     * Creates the metadata for the file to be uploaded.
     *
     * @param type     the type of the file
     * @param folderId the folder ID to upload the file to
     * @return the file metadata
     */
    private File createFileMetadata(String type, String folderId) {
        File file = new File();
        String fileName = generateFileName(type);
        file.setName(fileName);
        file.setParents(Collections.singletonList(folderId));

        return file;
    }

    /**
     * Generates a file name based on the type and current timestamp.
     * <p>
     * Name format: <i>type_timestamp.jpeg</i> (e.g. user_avatar_20210801_123456.jpeg)
     *
     * @param type the type of the file
     * @return the generated file name
     */
    private String generateFileName(String type) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return String.format("%s_%s.%s", type, timestamp, IMAGE_EXTENSION);
    }
}