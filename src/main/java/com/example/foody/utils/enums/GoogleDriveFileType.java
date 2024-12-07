package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoogleDriveFileType {
    USER_AVATAR("user-avatars"),
    RESTAURANT_PHOTO("restaurant-photos"),
    DISH_PHOTO("dish-photos");

    private final String folderIdKey;
}