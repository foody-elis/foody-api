package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing different types of Google Drive files.
 */
@Getter
@AllArgsConstructor
public enum GoogleDriveFileType {

    USER_AVATAR("user-avatars"),
    RESTAURANT_PHOTO("restaurant-photos"),
    DISH_PHOTO("dish-photos");

    private final String folderIdKey;
}