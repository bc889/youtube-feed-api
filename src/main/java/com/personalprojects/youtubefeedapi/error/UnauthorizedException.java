package com.personalprojects.youtubefeedapi.error;
import com.personalprojects.youtubefeedapi.error.enums.UnauthorizedErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends KnownHttpException {

    public UnauthorizedException(UnauthorizedErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
