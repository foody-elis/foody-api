package com.example.foody.exceptions.google_drive;

public class GoogleDriveFileDeleteException extends RuntimeException {
    public GoogleDriveFileDeleteException(String fileUrl) {
        super(String.format("Deleting the file with url '%s' from Google Drive failed.", fileUrl));
    }
}
