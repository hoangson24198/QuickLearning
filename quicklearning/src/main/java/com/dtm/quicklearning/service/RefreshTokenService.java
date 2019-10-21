package com.dtm.quicklearning.service;

import com.dtm.quicklearning.model.token.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken createRefreshToken();

    void verifyExpiration(RefreshToken token);

    void deleteById(Integer id);

}
