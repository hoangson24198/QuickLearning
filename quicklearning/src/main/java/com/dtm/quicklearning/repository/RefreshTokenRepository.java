package com.dtm.quicklearning.repository;


import com.dtm.quicklearning.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Override
    Optional<RefreshToken> findById(Integer id);

    Optional<RefreshToken> findByToken(String token);

}
