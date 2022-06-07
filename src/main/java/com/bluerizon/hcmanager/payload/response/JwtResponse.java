package com.bluerizon.hcmanager.payload.response;


import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.models.Utilisateurs;

import java.util.HashSet;
import java.util.Set;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
//  private Long id;
//  private String username;
//  private String email;
//  private Set<Profils> profils = new HashSet<>();
  private Utilisateurs user;
  private String refreshToken;

//  public JwtResponse(String token, Long id, String username, String email, Set<Profils> profils) {
//    this.token = token;
//    this.id = id;
//    this.username = username;
//    this.email = email;
//    this.profils = profils;
//  }
//
//  public JwtResponse(String token, Long id, String username, String email, Set<Profils> profils, String refreshToken) {
//    this.token = token;
//    this.id = id;
//    this.username = username;
//    this.email = email;
//    this.profils = profils;
//    this.refreshToken = refreshToken;
//  }


  public JwtResponse(String token, Utilisateurs user, String refreshToken) {
    this.token = token;
    this.user = user;
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

  public Utilisateurs getUser() {
    return user;
  }

  public void setUser(Utilisateurs user) {
    this.user = user;
  }

  //
//  public Long getId() {
//    return id;
//  }
//
//  public void setId(Long id) {
//    this.id = id;
//  }
//
//  public String getEmail() {
//    return email;
//  }
//
//  public void setEmail(String email) {
//    this.email = email;
//  }
//
//  public String getUsername() {
//    return username;
//  }
//
//  public void setUsername(String username) {
//    this.username = username;
//  }

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

//  public Set<Profils> getProfils() {
//    return profils;
//  }
//
//  public void setProfils(Set<Profils> profils) {
//    this.profils = profils;
//  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
