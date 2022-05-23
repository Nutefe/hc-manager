// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.RefreshTokens;

import java.util.Optional;

public interface RefreshTokenDao
{
    Optional<RefreshTokens> findByToken(final String token);
    
    RefreshTokens createRefreshToken(final String username);
    
    RefreshTokens verifyExpiration(final RefreshTokens token);
    
    int deleteByUserId(final Long userId);
}
