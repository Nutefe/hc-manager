package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.AssurancesDao;
import com.bluerizon.hcmanager.dao.FacturesDao;
import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.exception.TokenRefreshException;
import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.request.EtatRequest;
import com.bluerizon.hcmanager.payload.scheduling.DatabaseUtil;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.request.LoginRequest;
import com.bluerizon.hcmanager.payload.request.TokenRefreshRequest;
import com.bluerizon.hcmanager.payload.response.JwtResponse;
import com.bluerizon.hcmanager.payload.response.TokenRefreshResponse;
import com.bluerizon.hcmanager.security.jwt.JwtUtils;
import com.bluerizon.hcmanager.security.services.UserDetailsImpl;
import com.bluerizon.hcmanager.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.bluerizon.hcmanager.payload.helper.Helpers.getDateFromString;

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

  private final Object monitor = new Object();

  private final JdbcTemplate jdbcTemplate;
  @Autowired
  private AssurancesDao assurancesDao;
  @Autowired
  private FacturesDao facturesDao;

@Autowired
  public AuthController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @RequestMapping("/auth/dumpDb")
  @ResponseBody
  public void dumpDb() throws IOException, InterruptedException {
    DatabaseUtil.backup(
            "bluerizon",
            "admin1",
            "db_hc_manager",
            Helpers.path_file+ UUID.randomUUID().toString()+".sql"
    );
  }

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

//  @RequestMapping(value = "/auth/test/etat", method =  RequestMethod.GET)
//  public List<Factures> save(@Valid @RequestBody EtatRequest request) {
//    Assurances assurance = this.assurancesDao.findById(request.getAssurance().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
//    List<Factures> factures = this.facturesDao.etatEntreprise(assurance,
//            getDateFromString(request.getStart()),
//            getDateFromString(request.getEnd()));
//    return  factures;
//  }


}
