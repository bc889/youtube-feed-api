package com.personalprojects.youtubefeedapi.error.enums;

import lombok.Getter;

@Getter
public enum UnauthorizedErrorCode implements IErrorCode {
    INVALID_TOKEN_REQUEST(401001,
            "Invalid verification token");

    private final int code;
    private final String message;

    UnauthorizedErrorCode(int code, String message) {

        this.code = code;
        this.message = message;
    }
}
