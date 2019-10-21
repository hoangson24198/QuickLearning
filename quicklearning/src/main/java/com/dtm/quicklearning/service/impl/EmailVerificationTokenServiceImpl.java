package com.dtm.quicklearning.service.impl;

import com.dtm.quicklearning.model.eNum.TokenStatus;
import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.model.exception.InvalidTokenRequestException;
import com.dtm.quicklearning.model.token.EmailVerificationToken;
import com.dtm.quicklearning.repository.EmailVerificationTokenRepository;
import com.dtm.quicklearning.service.EmailVerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailVerificationTokenServiceImpl.class);

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Value("${app.token.email.verification.duration}")
    private Long emailVerificationTokenExpiryDuration;

    @Override
    public void createVerificationToken(User user, String token) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(token);
        emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        LOGGER.info("Generated Email verification token [" + emailVerificationToken + "]");
        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public EmailVerificationToken updateExistingTokenWithNameAndExpiry(EmailVerificationToken existingToken) {
        existingToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        existingToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        LOGGER.info("Updated Email verification token [" + existingToken + "]");
        return save(existingToken);
    }

    @Override
    public Optional<EmailVerificationToken> findByToken(String token) {
        return this.emailVerificationTokenRepository.findByToken(token);
    }

    @Override
    public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
        return this.emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void verifyExpiration(EmailVerificationToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("Email Verification Token", token.getToken(), "Expired token. Please issue a new request");
        }
    }
}
