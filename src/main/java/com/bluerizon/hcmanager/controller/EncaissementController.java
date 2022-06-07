// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.EncaissementsDao;
import com.bluerizon.hcmanager.dao.EntreprisesDao;
import com.bluerizon.hcmanager.dao.FacturesDao;
import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.exception.NotFoundRequestException;
import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.EncaissementPage;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EncaissementController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_encaissement_page}")
    private String url_encaissement_page;

    @Value("${app.url_encaissement_search_page}")
    private String url_encaissement_search_page;

    @Value("${app.url_encaissement_day_page}")
    private String url_encaissement_day_page;

    @Value("${app.url_encaissement_day_search_page}")
    private String url_encaissement_day_search_page;

    @Autowired
    private EncaissementsDao encaissementsDao;
    @Autowired
    private FacturesDao facturesDao;
    @Autowired
    private UtilisateursDao utilisateursDao;

    @GetMapping("/encaissement/{id}")
    public Encaissements getOne(@PathVariable("id") final Long id) {
        final Encaissements encaissement = this.encaissementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return encaissement;
    }

    @GetMapping("/encaissements")
    public List<Encaissements> getAll() {
        final List<Encaissements> encaissements= this.encaissementsDao.findByDeletedFalse();
        return encaissements;
    }


    @RequestMapping(value ="/encaissements/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public EncaissementPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Encaissements> encaissements = this.encaissementsDao.findByDeletedFalse(pageable);

        EncaissementPage pages = new EncaissementPage();

        Long total = this.encaissementsDao.countEncaissements();
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
            pages.setFirst_page_url(url_encaissement_page+1);
            pages.setLast_page_url(url_encaissement_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(encaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/encaissements/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EncaissementPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Encaissements> encaissements = this.encaissementsDao.recherche(s, pageable);

        EncaissementPage pages = new EncaissementPage();
        Long total = this.encaissementsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_encaissement_search_page+1+"/"+s);
            pages.setLast_page_url(url_encaissement_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(encaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/encaissements/day/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public EncaissementPage selectAllDayPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Encaissements> encaissements = this.encaissementsDao.findByDateEncaissementAndDeletedFalse(Helpers.getDateFromString(Helpers.currentDate()), pageable);

        EncaissementPage pages = new EncaissementPage();

        Long total = this.encaissementsDao.countEncaissements(Helpers.getDateFromString(Helpers.currentDate()));
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
            pages.setFirst_page_url(url_encaissement_day_page+1);
            pages.setLast_page_url(url_encaissement_day_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_day_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_day_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(encaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/encaissements/day/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EncaissementPage searchEncDayPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Encaissements> encaissements = this.encaissementsDao.recherche(Helpers.getDateFromString(Helpers.currentDate()), s, pageable);

        EncaissementPage pages = new EncaissementPage();
        Long total = this.encaissementsDao.countRecherche(Helpers.getDateFromString(Helpers.currentDate()), s);
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
            pages.setFirst_page_url(url_encaissement_day_search_page+1+"/"+s);
            pages.setLast_page_url(url_encaissement_day_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_day_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_day_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(encaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/encaissement", method =  RequestMethod.POST)
    public Encaissements save(@Valid @RequestBody Encaissements request, @CurrentUser final UserDetailsImpl currentUser) {
        Factures facture = this.facturesDao.findById(request.getFacture().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Utilisateurs utilisateur = this.utilisateursDao.findById(request.getFacture().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        if (!facture.isSolde()){
            if (facture.isEncaisse()){
                List<Encaissements> encaissements = this.encaissementsDao.findByFactureAndDeletedFalse(facture);
                Double initEnc = 0.0;
                for (Encaissements item :
                        encaissements) {
                    initEnc +=item.getMontant();
                }
                if ((facture.getTotal() - initEnc) >= request.getMontant()){
                    request.setFacture(facture);
                    request.setUtilisateur(utilisateur);
                    request.setDateEncaissement(new Date());
                    request.setReliquat(request.getTotal() - request.getMontant());
                    Encaissements paye = this.encaissementsDao.save(request);
                    facture.setEncaisse(true);
                    this.facturesDao.save(facture);
                    if (facture.getTotal() == (initEnc + paye.getMontant())){
                        facture.setSolde(true);
                        this.facturesDao.save(facture);
                    }
                } else {

                }
            } else {
                if (facture.getAcompte() > 0.0){
                    if (facture.getAcompte() == request.getMontant()){
                        request.setFacture(facture);
                        request.setUtilisateur(utilisateur);
                        request.setDateEncaissement(new Date());
                        request.setReliquat(request.getTotal() - request.getMontant());
                        Encaissements paye = this.encaissementsDao.save(request);
                        facture.setEncaisse(true);
                        this.facturesDao.save(facture);
                        if (facture.getTotal() == paye.getMontant()){
                            facture.setSolde(true);
                            this.facturesDao.save(facture);
                        }
                    } else {

                    }
                } else {
                    if (facture.getTotal() >= request.getMontant()){
                        request.setFacture(facture);
                        request.setUtilisateur(utilisateur);
                        request.setDateEncaissement(new Date());
                        request.setReliquat(request.getTotal() - request.getMontant());
                        Encaissements paye = this.encaissementsDao.save(request);
                        facture.setEncaisse(true);
                        this.facturesDao.save(facture);
                        if (facture.getTotal() == paye.getMontant()){
                            facture.setSolde(true);
                            this.facturesDao.save(facture);
                        }
                    } else {

                    }
                }

            }
        }
        return this.encaissementsDao.save(request);
    }

    @RequestMapping(value = "/encaissement/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Encaissements encaissementInit = this.encaissementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        encaissementInit.setDeleted(true);
        this.encaissementsDao.save(encaissementInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
