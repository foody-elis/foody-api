package com.example.foody.service;

import com.example.foody.utils.enums.GoogleDriveFileType;

/**
 * Service interface for Google Drive operations.
 */
public interface GoogleDriveService {

    /**
     * Uploads a base64 encoded image to Google Drive.
     *
     * @param base64Image the base64 encoded image to upload
     * @param fileType the type of the file to be uploaded
     * @return the URL of the uploaded image
     */
    String uploadBase64Image(String base64Image, GoogleDriveFileType fileType);

    /**
     * Deletes an image from Google Drive.
     *
     * @param fileUrl the URL of the image to delete
     */
    void deleteImage(String fileUrl);
}