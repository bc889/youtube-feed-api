package com.personalprojects.youtubefeedapi.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String exceptionName;
    private LocalDateTime timestamp;
    private String uri;
    private Integer errorCode;
    private String errorMessage;
}
