package com.dtm.quicklearning.service.impl;

import com.dtm.quicklearning.model.exception.TokenRefreshException;
import com.dtm.quicklearning.model.token.RefreshToken;
import com.dtm.quicklearning.repository.RefreshTokenRepository;
import com.dtm.quicklearning.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.token.refresh.duration}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return this.refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRefreshCount(0L);
        return refreshToken;
    }

    @Override
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new TokenRefreshException(token.getToken(), "Expired token. Please issue a new request");
        }
    }

    @Override
    public void deleteById(Integer id) {
        this.refreshTokenRepository.deleteById(id);
    }
}
