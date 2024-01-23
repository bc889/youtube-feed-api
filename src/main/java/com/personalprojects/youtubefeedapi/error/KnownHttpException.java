package com.personalprojects.youtubefeedapi.error;
import com.personalprojects.youtubefeedapi.error.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KnownHttpException extends RuntimeException {
    IErrorCode errorCode;
    String message;
}