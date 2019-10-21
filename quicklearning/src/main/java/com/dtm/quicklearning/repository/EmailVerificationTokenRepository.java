package com.dtm.quicklearning.repository;

import com.dtm.quicklearning.model.token.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Integer> {
    Optional<EmailVerificationToken> findByToken(String token);
}
