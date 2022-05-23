package com.bluerizon.hcmanager.payload.response;


import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.models.Utilisateurs;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Utilisateurs utilisateur;
  private String refreshToken;

  public JwtResponse(String token, Utilisateurs utilisateur, String refreshToken) {
    this.token = token;
    this.utilisateur = utilisateur;
    this.refreshToken = refreshToken;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Utilisateurs getUtilisateur() {
    return utilisateur;
  }

  public void setUtilisateur(Utilisateurs utilisateur) {
    this.utilisateur = utilisateur;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
