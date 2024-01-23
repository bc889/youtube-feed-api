package com.personalprojects.youtubefeedapi.error;

import com.personalprojects.youtubefeedapi.error.enums.BadRequestErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends KnownHttpException {

    public BadRequestException(BadRequestErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
