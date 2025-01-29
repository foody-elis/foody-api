package com.example.foody.exceptions.google_drive;

/**
 * Exception thrown when there is an error during the deletion of a file from Google Drive.
 */
public class GoogleDriveFileDeleteException extends RuntimeException {

    /**
     * Constructs a new GoogleDriveFileDeleteException with a formatted message indicating the file URL that caused the deletion error.
     *
     * @param fileUrl the URL of the file that failed to be deleted from Google Drive
     */
    public GoogleDriveFileDeleteException(String fileUrl) {
        super(String.format("Deleting the file with url '%s' from Google Drive failed.", fileUrl));
    }
}