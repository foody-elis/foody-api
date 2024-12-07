package com.example.foody.exceptions.google_drive;

import com.example.foody.utils.enums.GoogleDriveFileType;

public class GoogleDriveFileDeleteException extends RuntimeException {
    public GoogleDriveFileDeleteException(String fileUrl) {
        super("Deleting the file with url " + fileUrl + " from Google Drive failed.");
    }
}
