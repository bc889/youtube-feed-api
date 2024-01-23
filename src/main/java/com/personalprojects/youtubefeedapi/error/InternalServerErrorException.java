package com.personalprojects.youtubefeedapi.error;

import com.personalprojects.youtubefeedapi.error.enums.InternalServerErrorCode;

public class InternalServerErrorException extends KnownHttpException {

    public InternalServerErrorException(InternalServerErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
