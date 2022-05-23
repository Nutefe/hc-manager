// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.EncaissementsDao;
import com.bluerizon.hcmanager.dao.FacturesDao;
import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.EncaissementPage;
import com.bluerizon.hcmanager.payload.pages.FacturePage;
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
public class FactureController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_facture_page}")
    private String url_facture_page;

    @Value("${app.url_facture_search_page}")
    private String url_facture_search_page;

    @Value("${app.url_facture_solde_page}")
    private String url_facture_solde_page;

    @Value("${app.url_facture_solde_search_page}")
    private String url_facture_solde_search_page;

    @Value("${app.url_facture_enc_page}")
    private String url_facture_enc_page;

    @Value("${app.url_facture_enc_search_page}")
    private String url_facture_enc_search_page;

    @Autowired
    private FacturesDao facturesDao;

    @GetMapping("/facture/{id}")
    public Factures getOne(@PathVariable("id") final Long id) {
        final Factures facture = this.facturesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return facture;
    }

    @GetMapping("/factures")
    public List<Factures> getAll() {
        final List<Factures> factures= this.facturesDao.findByDeletedFalse();
        return factures;
    }


    @RequestMapping(value ="/factures/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FacturePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.facturesDao.findByDeletedFalse(pageable);

        FacturePage pages = new FacturePage();

        Long total = this.facturesDao.countFactures();
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
            pages.setFirst_page_url(url_facture_page+1);
            pages.setLast_page_url(url_facture_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(factures);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/factures/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FacturePage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.facturesDao.recherche(s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.facturesDao.countRecherche(s);
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
            pages.setFirst_page_url(url_facture_search_page+1+"/"+s);
            pages.setLast_page_url(url_facture_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(factures);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/factures/solde/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FacturePage selectAllSoldePage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.facturesDao.findByDeletedFalseAndSoldeTrue(pageable);

        FacturePage pages = new FacturePage();

        Long total = this.facturesDao.countFacturesSolde();
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
            pages.setFirst_page_url(url_facture_solde_page+1);
            pages.setLast_page_url(url_facture_solde_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_solde_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_solde_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(factures);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/factures/solde/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FacturePage searchSoldePage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.facturesDao.rechercheSolde( s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.facturesDao.countRechercheSolde( s);
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
            pages.setFirst_page_url(url_facture_solde_search_page+1+"/"+s);
            pages.setLast_page_url(url_facture_solde_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_solde_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_solde_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(factures);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/factures/enc/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FacturePage selectAllEncPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.facturesDao.findByDeletedFalseAndEncaisseTrue( pageable);

        FacturePage pages = new FacturePage();

        Long total = this.facturesDao.countFacturesEncaisse();
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
            pages.setFirst_page_url(url_facture_enc_page+1);
            pages.setLast_page_url(url_facture_enc_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_enc_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_enc_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(factures);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/factures/enc/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FacturePage searchEncDayPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.facturesDao.rechercheEncaisse( s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.facturesDao.countRechercheEncaisse( s);
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
            pages.setFirst_page_url(url_facture_enc_search_page+1+"/"+s);
            pages.setLast_page_url(url_facture_enc_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_enc_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_enc_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(factures);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


//    @RequestMapping(value = "/facture", method =  RequestMethod.POST)
//    public Encaissements save(@Valid @RequestBody Encaissements request, @CurrentUser final UserDetailsImpl currentUser) {
//        return this.encaissementsDao.save(request);
//    }

//    @RequestMapping(value = "/encaissement/{id}", method =  RequestMethod.PUT)
//    public Entreprises update(@Valid @RequestBody Entreprises request, @PathVariable("id") final Long id) {
//        Entreprises entrepriseInit = this.entreprisesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
//        entrepriseInit.setRaisonSocial(request.getRaisonSocial());
//        entrepriseInit.setNif(request.getNif());
//        entrepriseInit.setTelephone(request.getTelephone());
//        entrepriseInit.setAdresse(request.getAdresse());
//        return this.entreprisesDao.save(entrepriseInit);
//    }

    @RequestMapping(value = "/facture/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Factures factureInit = this.facturesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        factureInit.setDeleted(true);
        this.facturesDao.save(factureInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
