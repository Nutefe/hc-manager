// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.AssurancesDao;
import com.bluerizon.hcmanager.dao.EntreprisesDao;
import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.exception.NotFoundRequestException;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.bluerizon.hcmanager.payload.pages.AssurancePage;
import com.bluerizon.hcmanager.payload.pages.EntreprisePage;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class EntrepriseController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_entreprise_page}")
    private String url_entreprise_page;

    @Value("${app.url_entreprise_search_page}")
    private String url_entreprise_search_page;

    @Autowired
    private EntreprisesDao entreprisesDao;
    @Autowired
    private UtilisateursDao utilisateursDao;

    @GetMapping("/entreprise/{id}")
    public Entreprises getOne(@PathVariable("id") final Long id) {
        final Entreprises entreprise = this.entreprisesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));

        return entreprise;
    }

    @GetMapping("/entreprises")
    public List<Entreprises> getAll() {
        List<Entreprises> entreprises= this.entreprisesDao.findByDeletedFalse();
        return entreprises;
    }


    @RequestMapping(value ="/entreprises/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public EntreprisePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Entreprises> entreprises = this.entreprisesDao.findByDeletedFalse(pageable);

        EntreprisePage pages = new EntreprisePage();

        Long total = this.entreprisesDao.countEntreprises();
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
            pages.setFirst_page_url(url_entreprise_page+1);
            pages.setLast_page_url(url_entreprise_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_entreprise_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_entreprise_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(entreprises);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/entreprises/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EntreprisePage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Entreprises> entreprises = this.entreprisesDao.recherche(s, pageable);

        EntreprisePage pages = new EntreprisePage();
        Long total = this.entreprisesDao.countRecherche(s);
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
            pages.setFirst_page_url(url_entreprise_search_page+1+"/"+s);
            pages.setLast_page_url(url_entreprise_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_entreprise_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_entreprise_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(entreprises);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/entreprise", method =  RequestMethod.POST)
    public Entreprises save(@Valid @RequestBody Entreprises request, @CurrentUser final UserDetailsImpl currentUser) {
        request.setUtilisateur(this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new NotFoundRequestException("Error: object is not found.")));
        request.setRaisonSocial(request.getRaisonSocial().toUpperCase());
        return this.entreprisesDao.save(request);
    }

    @RequestMapping(value = "/entreprise/{id}", method =  RequestMethod.PUT)
    public Entreprises update(@Valid @RequestBody Entreprises request, @PathVariable("id") final Long id) {
        Entreprises entrepriseInit = this.entreprisesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        entrepriseInit.setRaisonSocial(request.getRaisonSocial().toUpperCase());
        entrepriseInit.setNif(request.getNif());
        entrepriseInit.setTelephone(request.getTelephone());
        entrepriseInit.setAdresse(request.getAdresse());
        return this.entreprisesDao.save(entrepriseInit);
    }

    @RequestMapping(value = "/entreprise/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Entreprises entrepriseInit = this.entreprisesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        entrepriseInit.setDeleted(true);
        this.entreprisesDao.save(entrepriseInit);
    }

    @RequestMapping(value = { "/check/raison/social/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkRaisonSocial(@PathVariable("s") final String s) {
        return this.entreprisesDao.existsByRaisonSocial(s);
    }

    @RequestMapping(value = { "/check/raison/social/update/{id}/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkRaisonSocialId(@PathVariable("s") final String s, @PathVariable("id") final Long id) {
        System.out.println(this.entreprisesDao.existsByRaisonSocial(s, id));
        return this.entreprisesDao.existsByRaisonSocial(s, id);
    }

    @RequestMapping(value = { "/check/telephone/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkTelephone(@PathVariable("s") final String s) {
        return this.entreprisesDao.existsByTelephone(s);
    }

    @RequestMapping(value = { "/check/telephone/update/{id}/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkTelephoneId(@PathVariable("s") final String s, @PathVariable("id") final Long id) {
        return this.entreprisesDao.existsByTelephone(s, id);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
