package com.dtm.quicklearning.service;

import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.model.token.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenService {
    void createVerificationToken(User user,String token);

    EmailVerificationToken updateExistingTokenWithNameAndExpiry(EmailVerificationToken existingToken);

    Optional<EmailVerificationToken> findByToken(String token);

    EmailVerificationToken save(EmailVerificationToken emailVerificationToken);

    String generateNewToken();

    void verifyExpiration(EmailVerificationToken token);

}
