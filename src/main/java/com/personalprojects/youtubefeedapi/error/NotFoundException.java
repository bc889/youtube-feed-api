package com.personalprojects.youtubefeedapi.error;

import com.personalprojects.youtubefeedapi.error.enums.NotFoundErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends KnownHttpException {

    public NotFoundException(NotFoundErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
