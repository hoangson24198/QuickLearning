package com.dtm.quicklearning.service.impl;

import com.dtm.quicklearning.model.entity.PasswordResetToken;
import com.dtm.quicklearning.model.exception.InvalidTokenRequestException;
import com.dtm.quicklearning.repository.PasswordResetTokenRepository;
import com.dtm.quicklearning.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return this.passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return this.passwordResetTokenRepository.findByTokenName(token);
    }

    @Override
    public PasswordResetToken createToken() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        String token = UUID.randomUUID().toString();
        passwordResetToken.setTokenName(token);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
        return passwordResetToken;
    }

    @Override
    public void verifyExpiration(PasswordResetToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("Password Reset Token", token.getTokenName(),
                    "Expired token. Please issue a new request");
        }
    }
}
