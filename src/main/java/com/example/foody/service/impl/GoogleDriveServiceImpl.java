package com.example.foody.service.impl;

import com.example.foody.config.GoogleDriveConfig;
import com.example.foody.exceptions.google_drive.GoogleDriveFileDeleteException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.utils.enums.GoogleDriveFileType;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Component
public class GoogleDriveServiceImpl implements GoogleDriveService {
    private final Drive drive;
    private final Map<String, String> folders;

    private static final String GOOGLE_DRIVE_URL = "https://drive.google.com/uc?id=";
    private static final String IMAGE_MIME_TYPE = "image/jpeg";
    private static final String IMAGE_EXTENSION = "jpeg";

    public GoogleDriveServiceImpl(
            Drive drive,
            GoogleDriveConfig googleDriveConfig) {
        this.drive = drive;
        this.folders = googleDriveConfig.getFOLDERS();
    }

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

    @Override
    public void deleteImage(String fileUrl) {
        try {
            String fileId = fileUrl.substring(GOOGLE_DRIVE_URL.length());
            drive.files().delete(fileId).execute();
        } catch (Exception e) {
            throw new GoogleDriveFileDeleteException(fileUrl);
        }
    }

    private String getFolderId(GoogleDriveFileType fileType) {
        String folderId = folders.get(fileType.getFolderIdKey());
        if (folderId == null) {
            throw new IllegalArgumentException("Folder ID not found for file type: " + fileType);
        }

        return folderId;
    }

    private String uploadImage(byte[] image, String type, String folderId) throws IOException {
        File file = createFileMetadata(type, folderId);
        com.google.api.client.http.InputStreamContent mediaContent =
                new com.google.api.client.http.InputStreamContent(IMAGE_MIME_TYPE, new ByteArrayInputStream(image));

        File uploadedFile = drive.files().create(file, mediaContent)
                .setFields("id")
                .execute();

        return uploadedFile.getId();
    }

    private String generatePublicLink(String fileId) throws IOException {
        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");
        drive.permissions().create(fileId, permission).execute();

        return GOOGLE_DRIVE_URL + fileId;
    }

    private File createFileMetadata(String type, String folderId) {
        File file = new File();
        String fileName = generateFileName(type);
        file.setName(fileName);
        file.setParents(Collections.singletonList(folderId));

        return file;
    }

    // Name format: type_timestamp.jpeg, e.g. user_avatar_20210801_123456.jpeg
    private String generateFileName(String type) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return String.format("%s_%s.%s", type, timestamp, IMAGE_EXTENSION);
    }
}