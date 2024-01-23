package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class VerificationTokenService implements IVerificationTokenService {

    private final String verificationToken;

    public VerificationTokenService() {
        this.verificationToken = generateRandomToken();
    }

    @Override
    public String getVerificationToken() {
        return verificationToken;
    }

    private String generateRandomToken() {
        byte[] tokenBytes = new byte[32];
        new SecureRandom().nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
