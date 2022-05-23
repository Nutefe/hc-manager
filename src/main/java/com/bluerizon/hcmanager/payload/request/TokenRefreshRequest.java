// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.payload.request;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest
{
    @NotBlank
    private String refreshToken;
    
    public String getRefreshToken() {
        return this.refreshToken;
    }
    
    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
