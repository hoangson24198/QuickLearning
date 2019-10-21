package com.dtm.quicklearning.service;

import com.dtm.quicklearning.model.entity.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.Optional;

public interface PasswordResetTokenService {
    PasswordResetToken save(PasswordResetToken passwordResetToken);

    Optional<PasswordResetToken> findByToken(String token);

    PasswordResetToken createToken();

    void verifyExpiration(PasswordResetToken token);
}
