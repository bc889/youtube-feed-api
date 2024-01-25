package com.personalprojects.youtubefeedapi.error.enums;

import lombok.Getter;

@Getter
public enum BadRequestErrorCode implements IErrorCode {
    DUPLICATE_TOPIC_URL(400001,
            "A subscription with the provided topic url already exists."),
    MISSING_TOPIC_URL(400002, "No topic url provided."),
    MISSING_NAME(400003, "No name provided"),
    INVALID_USER_ID(400004, "No user with the provided user Id exists."),
    SUBSCRIPTION_MISMATCH(
            400005,
            "The retrieved subscription's data doesn't match the given notification's."
    ),
    FEED_PARSING_ERROR(
            400006,
            "Couldn't parse the given atom feed."
    ),
    DATE_PARSING_ERROR(
            400007,
            "The given date was invalid."
    );
    private final int code;
    private final String message;

    BadRequestErrorCode(int code, String message) {

        this.code = code;
        this.message = message;
    }

}
