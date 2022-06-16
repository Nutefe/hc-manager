// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.ProfilsDao;
import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.exception.NotFoundRequestException;
import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.bluerizon.hcmanager.payload.pages.UtilisateurPage;
import com.bluerizon.hcmanager.payload.request.PasswordConnectRequest;
import com.bluerizon.hcmanager.payload.request.PasswordRequest;
import com.bluerizon.hcmanager.payload.request.SignupRequest;
import com.bluerizon.hcmanager.payload.response.MessageResponse;
import com.bluerizon.hcmanager.security.jwt.CurrentUser;
import com.bluerizon.hcmanager.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UtilisateurController
{

    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_utilisateur_all_page}")
    private String url_utilisateur_all_page;

    @Value("${app.url_utilisateur_all_search_page}")
    private String url_utilisateur_all_search_page;

    @Autowired
    private UtilisateursDao utilisateursDao;

    @Autowired
    private ProfilsDao profilsDao;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @RequestMapping(value = "/utilisateur/me", method = RequestMethod.GET )
    public Utilisateurs getCurrentUser(@CurrentUser final UserDetailsImpl currentUser) {
        final Utilisateurs users = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));

        return users;
    }

    @RequestMapping(value = "/utilisateur/{id}", method = RequestMethod.GET )
    public Utilisateurs getOneUser(@PathVariable("id") final Long id) {
        Utilisateurs users = utilisateursDao.findById(id).orElseThrow(() -> new NotFoundRequestException("Objet dont l'id "+id+" n'existe pas!"));

        return users;
    }

    @RequestMapping(value = "/utilisateur", method = RequestMethod.GET )
    public List<Utilisateurs> getAll() {
        List<Utilisateurs> utilisateurs=utilisateursDao.findByDeletedFalseOrderByIdDesc();

        return utilisateurs;
    }

    @RequestMapping(value = "/utilisateurs", method = RequestMethod.GET )
    public List<Utilisateurs> getAll(@CurrentUser final UserDetailsImpl currentUser) {
        List<Utilisateurs> utilisateurs=utilisateursDao.selectUser(currentUser.getId());
        return utilisateurs;
    }


    @RequestMapping(value ="/utilisateurs/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public UtilisateurPage selectAllPage(@PathVariable(value = "page") int page,
                                         @CurrentUser final UserDetailsImpl currentUser) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Utilisateurs> utilisateurs = this.utilisateursDao.selectAllUsers(currentUser.getId(), pageable);

        UtilisateurPage pages = new UtilisateurPage();

        Long total = this.utilisateursDao.countUser(currentUser.getId());
        Long lastPage;

        if (total > 0){
            pages.setTotal(total);
            pages.setPer_page(page_size);
            pages.setCurrent_page(page);
            if (total % page_size == 0){
                lastPage = total/page_size;
            } else {
                lastPage = (total/page_size)+1;

            }
            pages.setLast_page(lastPage);
            pages.setFirst_page_url(url_utilisateur_all_page+1);
            pages.setLast_page_url(url_utilisateur_all_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_utilisateur_all_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_utilisateur_all_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(utilisateurs);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/utilisateurs/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public UtilisateurPage searchUtilisateurPage(@PathVariable(value = "page") int page,
                                                  @PathVariable(value = "s") String s,
                                                  @CurrentUser final UserDetailsImpl currentUser){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Utilisateurs> utilisateurs = this.utilisateursDao.recherche(s, currentUser.getId(), pageable);

        UtilisateurPage pages = new UtilisateurPage();
        Long total = this.utilisateursDao.countRecherche(s, currentUser.getId());
        Long lastPage;

        if (total > 0){
            pages.setTotal(total);
            pages.setPer_page(page_size);
            pages.setCurrent_page(page);

            if (total %page_size == 0){
                lastPage = total/page_size;
            } else {
                lastPage = (total/page_size)+1;
            }
            pages.setLast_page(lastPage);
            pages.setFirst_page_url(url_utilisateur_all_search_page+1+"/"+s);
            pages.setLast_page_url(url_utilisateur_all_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_utilisateur_all_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_utilisateur_all_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(utilisateurs);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @PostMapping("/utilisateur")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (utilisateursDao.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (utilisateursDao.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Utilisateurs utilisateur = new Utilisateurs(
                signUpRequest.getUsername(),
                signUpRequest.getPassword(),
                signUpRequest.getEmail(),
                signUpRequest.getNom(),
                signUpRequest.getPrenom()
        );

        List<Profils> profils = signUpRequest.getProfils();
        Set<Profils> profilsSet = new HashSet<>();

        if (profils.size()>0) {
            profils.forEach(item -> {
                Profils profil = profilsDao.findById(item.getId()).orElseThrow(() -> new RuntimeException("Error: Ccom is not found."));
                profilsSet.add(profil);
            });
        }
        utilisateur.setProfils(profilsSet);

        utilisateursDao.save(utilisateur);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @RequestMapping(value = "/utilisateur/connect", method =  RequestMethod.PUT)
    public ResponseEntity<?> updateUserConnect(@Valid @RequestBody SignupRequest signUpRequest,
                                               @CurrentUser final UserDetailsImpl currentUser) {

        if (utilisateursDao.existsByUsername(signUpRequest.getUsername(), currentUser.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (utilisateursDao.existsByEmail(signUpRequest.getEmail(), currentUser.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Utilisateurs utilisateur = utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));

        utilisateur.setUsername(signUpRequest.getUsername());
        utilisateur.setEmail(signUpRequest.getEmail());
        utilisateur.setNom(signUpRequest.getNom());
        utilisateur.setPrenom(signUpRequest.getPrenom());

        utilisateursDao.update(utilisateur);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @RequestMapping(value = "/utilisateur/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody SignupRequest signUpRequest, @PathVariable("id") final Long id) {

        if (utilisateursDao.existsByUsername(signUpRequest.getUsername(), id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (utilisateursDao.existsByEmail(signUpRequest.getEmail(), id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Utilisateurs utilisateur = utilisateursDao.findById(id).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));
        Set<Profils> profilSet = utilisateur.getProfils();
        utilisateur.getProfils().removeAll(profilSet);
        utilisateur.setUsername(signUpRequest.getUsername());
        utilisateur.setEmail(signUpRequest.getEmail());
        utilisateur.setNom(signUpRequest.getNom());
        utilisateur.setPrenom(signUpRequest.getPrenom());

        List<Profils> profils = signUpRequest.getProfils();
        Set<Profils> profilsSet = new HashSet<>();

        if (profils.size()>0) {
            profils.forEach(item -> {
                Profils profil = profilsDao.findById(item.getId()).orElseThrow(() -> new RuntimeException("Error: Ccom is not found."));
                profilsSet.add(profil);
            });
        }
        utilisateur.setProfils(profilsSet);

        utilisateursDao.update(utilisateur);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @RequestMapping(value = "/utilisateur/password", method =  RequestMethod.PUT)
    public ResponseEntity<MessageResponse> updatePasswordUserConnect(@Valid @RequestBody PasswordConnectRequest request,
                               @CurrentUser final UserDetailsImpl currentUser) {
        Utilisateurs utilisateur = utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));
        if (passwordEncoder.matches(request.getAncien(), utilisateur.getPassword())){
            utilisateursDao.updatePassword(currentUser.getId(), passwordEncoder.encode(request.getNouveau()));
            return ResponseEntity.ok(new MessageResponse("Mot de passe modifier avec succes!"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: mot de passe non modifier!"));
        }

    }

    @RequestMapping(value = "/utilisateur/password/{id}", method =  RequestMethod.PUT)
    public void updatePassword(@Valid @RequestBody PasswordRequest request, @PathVariable("id") final Long id) {
        utilisateursDao.updatePassword(id, passwordEncoder.encode(request.getNouveau()));
    }

    @RequestMapping(value = "/utilisateur/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Utilisateurs utilisateur = utilisateursDao.findById(id).orElseThrow(() -> new NotFoundRequestException("Objet dont l'id "+id+" n'existe pas!"));
        utilisateur.setDeleted(true);
        utilisateursDao.save(utilisateur);
    }

    @RequestMapping(value = { "/check/username/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkUsername(@PathVariable("s") final String s) {
        return this.utilisateursDao.existsByUsername(s);
    }

    @RequestMapping(value = { "/check/username/update/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkUsername(@PathVariable("s") final String s, @CurrentUser final UserDetailsImpl currentUser) {
        final Utilisateurs users = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));
        return this.utilisateursDao.existsByUsername(s, users.getId());
    }

    @RequestMapping(value = { "/check/username/update/{id}/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkUsernameId(@PathVariable("s") final String s, @PathVariable("id") final Long id) {
        final Utilisateurs users = this.utilisateursDao.findById(id).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));
        return this.utilisateursDao.existsByUsername(s, users.getId());
    }

    @RequestMapping(value = { "/check/email/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkEmail(@PathVariable("s") final String s) {
        return this.utilisateursDao.existsByEmail(s);
    }

    @RequestMapping(value = { "/check/email/update/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkEmail(@PathVariable("s") final String s, @CurrentUser final UserDetailsImpl currentUser) {
        final Utilisateurs users = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));
        return this.utilisateursDao.existsByEmail(s, users.getId());
    }

    @RequestMapping(value = { "/check/email/update/{id}/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkEmailId(@PathVariable("s") final String s, @PathVariable("id") final Long id) {
        final Utilisateurs users = this.utilisateursDao.findById(id).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));
        return this.utilisateursDao.existsByEmail(s, users.getId());
    }

    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
