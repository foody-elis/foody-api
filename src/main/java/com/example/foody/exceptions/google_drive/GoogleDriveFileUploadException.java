package com.example.foody.exceptions.google_drive;

import com.example.foody.utils.enums.GoogleDriveFileType;

public class GoogleDriveFileUploadException extends RuntimeException {
    public GoogleDriveFileUploadException(GoogleDriveFileType fileType) {
        super(String.format("Uploading the %s file to Google Drive failed.", fileType.name().toLowerCase()));
    }
}