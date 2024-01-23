package com.personalprojects.youtubefeedapi.error;

import com.personalprojects.youtubefeedapi.error.enums.InternalServerErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Catches and handles all common http errors thrown out by REST services
 * and returns the corresponding error objects.
 */
@ControllerAdvice
public class CommonHttpExceptionHandler {
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(
            InternalServerErrorException e, HttpServletRequest request) {

        return buildResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e, HttpServletRequest request) {

        return buildResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException e, HttpServletRequest request) {

        return buildResponse(e, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e, HttpServletRequest request) {

        return buildResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(
            InternalServerErrorException e, HttpServletRequest request) {

        return buildResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            KnownHttpException e,
            HttpServletRequest request,
            HttpStatus httpStatus) {

        ErrorResponse responseBody = ErrorResponse.builder()
                .exceptionName(e.getClass().getName())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .errorCode(e.getErrorCode().getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(httpStatus)
                .body(responseBody);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            Exception e,
            HttpServletRequest request,
            HttpStatus httpStatus) {

        ErrorResponse responseBody = ErrorResponse.builder()
                .exceptionName(e.getClass().getName())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .errorCode(InternalServerErrorCode.UNHANDLED_ERROR.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(httpStatus)
                .body(responseBody);
    }

}
