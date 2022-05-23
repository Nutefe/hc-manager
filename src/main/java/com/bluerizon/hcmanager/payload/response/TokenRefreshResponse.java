// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.payload.response;

public class TokenRefreshResponse
{
    private String token;
    private String refreshToken;
    private String tokenType;
    
    public TokenRefreshResponse(final String token, final String refreshToken) {
        this.tokenType = "Bearer";
        this.token = token;
        this.refreshToken = refreshToken;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public String getRefreshToken() {
        return this.refreshToken;
    }
    
    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getTokenType() {
        return this.tokenType;
    }
    
    public void setTokenType(final String tokenType) {
        this.tokenType = tokenType;
    }
}
