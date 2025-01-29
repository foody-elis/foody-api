package com.example.foody.exceptions.google_drive;

import com.example.foody.utils.enums.GoogleDriveFileType;

/**
 * Exception thrown when there is an error during the upload of a file to Google Drive.
 */
public class GoogleDriveFileUploadException extends RuntimeException {

    /**
     * Constructs a new GoogleDriveFileUploadException with a formatted message indicating the file type that caused the upload error.
     *
     * @param fileType the type of the file that failed to be uploaded to Google Drive
     */
    public GoogleDriveFileUploadException(GoogleDriveFileType fileType) {
        super(String.format("Uploading the %s file to Google Drive failed.", fileType.name().toLowerCase()));
    }
}