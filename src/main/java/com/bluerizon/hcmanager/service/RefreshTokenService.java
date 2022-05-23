// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.RefreshTokenDao;
import com.bluerizon.hcmanager.exception.TokenRefreshException;
import com.bluerizon.hcmanager.models.RefreshTokens;
import com.bluerizon.hcmanager.repository.RefreshTokenRepository;
import com.bluerizon.hcmanager.repository.UtilisateursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements RefreshTokenDao
{
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UtilisateursRepository userRepository;
    @Value("${stock_new_world.app.jwtExpirationMs}")
    private int jwtExpirationInMs;
    @Value("${stock_new_world.app.refreshExpirationDateInMs}")
    private Long refreshTokenDurationMs;
    
    @Override
    public Optional<RefreshTokens> findByToken(final String token) {
        return this.refreshTokenRepository.findByToken(token);
    }
    
    @Override
    public RefreshTokens createRefreshToken(final String username) {
        RefreshTokens refreshTokens = new RefreshTokens();
        refreshTokens.setUtilisateur(this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Error: username is not found.")));
        refreshTokens.setExpiryDate(Instant.now().plusMillis(this.refreshTokenDurationMs));
        refreshTokens.setToken(UUID.randomUUID().toString());
        refreshTokens = this.refreshTokenRepository.save(refreshTokens);
        return refreshTokens;
    }
    
    @Override
    public RefreshTokens verifyExpiration(final RefreshTokens token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
    
    @Override
    public int deleteByUserId(final Long userId) {
        return 0;
    }
}
