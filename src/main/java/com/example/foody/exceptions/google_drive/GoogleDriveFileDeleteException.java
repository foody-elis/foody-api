package com.example.foody.exceptions.google_drive;

public class GoogleDriveFileDeleteException extends RuntimeException {
    public GoogleDriveFileDeleteException(String fileUrl) {
        super("Deleting the file with url " + fileUrl + " from Google Drive failed.");
    }
}
