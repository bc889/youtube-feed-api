package com.personalprojects.youtubefeedapi.error.enums;

import lombok.Getter;

@Getter
public enum InternalServerErrorCode implements IErrorCode {

    UNHANDLED_ERROR(500001,
            "Unhandled exception");

    private final int code;
    private final String message;

    InternalServerErrorCode(int code, String message) {

        this.code = code;
        this.message = message;
    }
}

