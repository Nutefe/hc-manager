// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.CaisseDao;
import com.bluerizon.hcmanager.dao.LigneCaisseDao;
import com.bluerizon.hcmanager.dao.ReserveDao;
import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.exception.NotFoundRequestException;
import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.pages.CaissePage;
import com.bluerizon.hcmanager.payload.pages.ReservePage;
import com.bluerizon.hcmanager.payload.request.CaisseRequest;
import com.bluerizon.hcmanager.payload.response.LigneResponse;
import com.bluerizon.hcmanager.security.jwt.CurrentUser;
import com.bluerizon.hcmanager.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CaisseController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_caisses_page}")
    private String url_caisses_page;

    @Value("${app.url_caisses_search_page}")
    private String url_caisses_search_page;

    @Autowired
    private CaisseDao caisseDao;
    @Autowired
    private LigneCaisseDao ligneCaisseDao;
    @Autowired
    private UtilisateursDao utilisateursDao;

    @GetMapping("/caisse/{id}")
    public Caisses getOne(@PathVariable("id") final Integer id) {
        final Caisses caisse = this.caisseDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return caisse;
    }
    @GetMapping("/caisse/utilisateur")
    public LigneCaisses getOne(@CurrentUser final UserDetailsImpl currentUser) {
        final Utilisateurs users = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new NotFoundRequestException("Objet n'existe pas!"));
        final LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUserCaisse(users);
        return ligneCaisse;
    }

    @GetMapping("/caisses")
    public List<Caisses> getAll() {
        List<Caisses> caisses= this.caisseDao.findByDeletedFalse();
        return caisses;
    }

    @RequestMapping(value ="/caisses/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public CaissePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Caisses> caisses = this.caisseDao.findByDeletedFalse(pageable);
        List<LigneResponse> responses = new ArrayList<>();
        for (Caisses item :
                caisses) {
            List<Utilisateurs> utilisateurs = new ArrayList<>();
            List<LigneCaisses> ligneCaisses = this.ligneCaisseDao.findByCaisse(item);
            for (LigneCaisses ligne :
                    ligneCaisses) {
                utilisateurs.add(ligne.getCaissePK().getUtilisateur());
            }
            responses.add(new LigneResponse(item, utilisateurs));
        }

        CaissePage pages = new CaissePage();

        Long total = this.caisseDao.countReserves();
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
            pages.setFirst_page_url(url_caisses_page+1);
            pages.setLast_page_url(url_caisses_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_caisses_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_caisses_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(responses);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/caisses/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CaissePage searchProfilPage(@PathVariable(value = "page") int page,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Caisses> caisses = this.caisseDao.recherche(s, pageable);
        List<LigneResponse> responses = new ArrayList<>();
        for (Caisses item :
                caisses) {
            List<Utilisateurs> utilisateurs = new ArrayList<>();
            List<LigneCaisses> ligneCaisses = this.ligneCaisseDao.findByCaisse(item);
            for (LigneCaisses ligne :
                    ligneCaisses) {
                utilisateurs.add(ligne.getCaissePK().getUtilisateur());
            }
            responses.add(new LigneResponse(item, utilisateurs));
        }

        CaissePage pages = new CaissePage();
        Long total = this.caisseDao.countRecherche(s);
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
            pages.setFirst_page_url(url_caisses_search_page+1+"/"+s);
            pages.setLast_page_url(url_caisses_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_caisses_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_caisses_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(responses);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/ligne/caisse", method =  RequestMethod.POST)
    public LigneResponse save(@Valid @RequestBody CaisseRequest request) {
        Caisses caisse = new Caisses();
        caisse.setLibelle(request.getLibelle().toUpperCase());
        caisse.setMontant(request.getMontant());
        caisse.setSolde(request.getMontant());
        caisse.setRecette(0.0);
        caisse.setDecaissement(0.0);
        Caisses caisseSave = this.caisseDao.save(caisse);
        List<Utilisateurs> utilisateurs = new ArrayList<>();
        for (Utilisateurs utilisateur :
                request.getUtilisateurs()) {
            LigneCaisses ligne = new LigneCaisses();
            Utilisateurs u = this.utilisateursDao.findById(utilisateur.getId())
                    .orElseThrow(() -> new RuntimeException("Error: object is not found."));
            ligne.setCaissePK(new CaissePK(caisseSave, u));
            this.ligneCaisseDao.save(ligne);
            utilisateurs.add(u);
        }

        return new LigneResponse(caisseSave, utilisateurs);
    }

    @RequestMapping(value = "/ligne/caisse/{id}", method =  RequestMethod.PUT)
    public LigneResponse updateLigne(@Valid @RequestBody CaisseRequest request,
                                     @PathVariable("id") final Integer id) {
        final Caisses caisse = this.caisseDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        caisse.setLibelle(request.getLibelle().toUpperCase());
        caisse.setMontant(caisse.getMontant()+request.getMontant());
        caisse.setSolde(caisse.getSolde()+request.getMontant());
        Caisses caisseSave = this.caisseDao.save(caisse);
        List<Utilisateurs> utilisateurs = new ArrayList<>();
        List<LigneCaisses> ligneCaisses = this.ligneCaisseDao.findByCaisse(caisse);
        for (LigneCaisses item :
                ligneCaisses) {
            item.setDeleted(true);
            this.ligneCaisseDao.save(item);
        }
        for (Utilisateurs utilisateur :
                request.getUtilisateurs()) {

            Utilisateurs u = this.utilisateursDao.findById(utilisateur.getId())
                    .orElseThrow(() -> new RuntimeException("Error: object is not found."));
            LigneCaisses ligneInit = this.ligneCaisseDao.findFirstByUser(u);
            if (ligneInit == null){
                LigneCaisses ligne = new LigneCaisses();
                ligne.setCaissePK(new CaissePK(caisseSave, u));
                this.ligneCaisseDao.save(ligne);
            } else {
                ligneInit.setDeleted(false);
                this.ligneCaisseDao.save(ligneInit);
            }

            utilisateurs.add(u);
        }

        return new LigneResponse(caisseSave, utilisateurs);
    }

    @RequestMapping(value = "/caisse", method =  RequestMethod.POST)
    public Caisses save(@Valid @RequestBody Caisses request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        request.setMontant(request.getMontant());
        return this.caisseDao.save(request);
    }

    @RequestMapping(value = "/caisse/{id}", method =  RequestMethod.PUT)
    public Caisses update(@Valid @RequestBody Caisses request, @PathVariable("id") final Integer id) {
        Caisses caisseInit = this.caisseDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        caisseInit.setLibelle(request.getLibelle().toUpperCase());
        caisseInit.setMontant(request.getMontant());
        return this.caisseDao.save(caisseInit);
    }

    @RequestMapping(value = "/caisse/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        Caisses caisseInit = caisseDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        caisseInit.setDeleted(true);
        this.caisseDao.save(caisseInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
