package com.example.foody.service;

import com.example.foody.utils.enums.GoogleDriveFileType;

public interface GoogleDriveService {
    String uploadBase64Image(String base64Image, GoogleDriveFileType fileType);

    void deleteImage(String fileUrl);
}