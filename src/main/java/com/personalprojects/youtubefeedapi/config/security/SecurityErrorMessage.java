package com.personalprojects.youtubefeedapi.config.security;

public record SecurityErrorMessage(String message) {

    public static SecurityErrorMessage from(final String message) {
        return new SecurityErrorMessage(message);
    }
}
