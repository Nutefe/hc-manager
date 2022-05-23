package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.exception.TokenRefreshException;
import com.bluerizon.hcmanager.models.RefreshTokens;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.bluerizon.hcmanager.payload.request.LoginRequest;
import com.bluerizon.hcmanager.payload.request.TokenRefreshRequest;
import com.bluerizon.hcmanager.payload.response.JwtResponse;
import com.bluerizon.hcmanager.payload.response.TokenRefreshResponse;
import com.bluerizon.hcmanager.security.jwt.JwtUtils;
import com.bluerizon.hcmanager.security.services.UserDetailsImpl;
import com.bluerizon.hcmanager.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UtilisateursDao utilisateursDao;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/auth/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Utilisateurs utilisateur = utilisateursDao.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: Profil is not found."));

    final RefreshTokens refreshTokens = this.refreshTokenService.createRefreshToken(userDetails.getUsername());
    return ResponseEntity.ok(new JwtResponse(jwt,
                          utilisateur,
                          refreshTokens.getToken()));
  }

  @PostMapping({ "/auth/refreshtoken" })
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody final TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshTokens::getUtilisateur)
            .map(user -> {
              String token = jwtUtils.generateJwtUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }

}
