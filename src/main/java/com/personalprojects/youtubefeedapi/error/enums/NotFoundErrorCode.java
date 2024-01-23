package com.personalprojects.youtubefeedapi.error.enums;

import lombok.Getter;

@Getter
public enum NotFoundErrorCode implements IErrorCode {
    SUBSCRIPTION_ID_NOT_FOUND(404001,
            "No subscription with the provided id found in the database"),
    USER_ID_NOT_FOUND(404002, "No user with the provided id found in the database."),
    FEED_ENTRY_ID_NOT_FOUND(404003, "No feed entry with the provided id found in the database.");

    private final int code;
    private final String message;

    NotFoundErrorCode(int code, String message) {

        this.code = code;
        this.message = message;
    }
}
