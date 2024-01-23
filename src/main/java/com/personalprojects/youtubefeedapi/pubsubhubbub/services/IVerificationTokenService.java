package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

/**
 * Generates the verification token to be provided by the Pubsub server.
 */
public interface IVerificationTokenService {
    String getVerificationToken();
}
