// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.*;
import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.DecaissementPage;
import com.bluerizon.hcmanager.payload.pages.DepenseReservePage;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DecaissementController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_decaissements_page}")
    private String url_decaissements_page;

    @Value("${app.url_decaissements_search_page}")
    private String url_decaissements_search_page;

    @Value("${app.url_decaissements_today_page}")
    private String url_decaissements_today_page;

    @Value("${app.url_decaissements_today_search_page}")
    private String url_decaissements_today_search_page;

    @Value("${app.url_decaissements_date_page}")
    private String url_decaissements_date_page;

    @Value("${app.url_decaissements_date_search_page}")
    private String url_decaissements_date_search_page;

    @Value("${app.url_decaissements_caisse_page}")
    private String url_decaissements_caisse_page;

    @Value("${app.url_decaissements_caisse_search_page}")
    private String url_decaissements_caisse_search_page;

    @Value("${app.url_decaissements_caisse_today_page}")
    private String url_decaissements_caisse_today_page;

    @Value("${app.url_decaissements_caisse_today_search_page}")
    private String url_decaissements_caisse_today_search_page;

    @Value("${app.url_decaissements_caisse_date_page}")
    private String url_decaissements_caisse_date_page;

    @Value("${app.url_decaissements_caisse_date_search_page}")
    private String url_decaissements_caisse_date_search_page;

    @Autowired
    private DecaissementDao decaissementDao;
    @Autowired
    private LigneCaisseDao ligneCaisseDao;
    @Autowired
    private UtilisateursDao utilisateursDao;
    @Autowired
    private CaisseDao caisseDao;

    @GetMapping("/decaissement/{id}")
    public Decaissements getOne(@PathVariable("id") final Long id) {
        final Decaissements decaissement = this.decaissementDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return decaissement;
    }

    @GetMapping("/decaissements")
    public List<Decaissements> getAll() {
        List<Decaissements> decaissements= this.decaissementDao.findByDeletedFalse();
        return decaissements;
    }

    @RequestMapping(value ="/decaissements/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DecaissementPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Decaissements> decaissements = this.decaissementDao.findByDeletedFalse(pageable);

        DecaissementPage pages = new DecaissementPage();

        Long total = this.decaissementDao.countDecaissements();
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
            pages.setFirst_page_url(url_decaissements_page+1);
            pages.setLast_page_url(url_decaissements_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(decaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/decaissements/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DecaissementPage searchProfilPage(@PathVariable(value = "page") int page,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Decaissements> decaissements = this.decaissementDao.recherche(s, pageable);

        DecaissementPage pages = new DecaissementPage();
        Long total = this.decaissementDao.countRecherche(s);
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
            pages.setFirst_page_url(url_decaissements_search_page+1+"/"+s);
            pages.setLast_page_url(url_decaissements_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(decaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/decaissements/today/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DecaissementPage selectAllPageToday(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Decaissements> decaissements = this.decaissementDao.findByDateDecaissementAndDeletedFalse(Helpers.getDateFromString(Helpers.currentDate()), pageable);

        DecaissementPage pages = new DecaissementPage();

        Long total = this.decaissementDao.countDecaissements(Helpers.getDateFromString(Helpers.currentDate()));
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
            pages.setFirst_page_url(url_decaissements_today_page+1);
            pages.setLast_page_url(url_decaissements_today_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_today_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_today_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(decaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/decaissements/today/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DecaissementPage searchDecaissementPageToday(@PathVariable(value = "page") int page,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Decaissements> decaissements = this.decaissementDao.recherche(Helpers.getDateFromString(Helpers.currentDate()), s, pageable);

        DecaissementPage pages = new DecaissementPage();
        Long total = this.decaissementDao.countRecherche(Helpers.getDateFromString(Helpers.currentDate()), s);
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
            pages.setFirst_page_url(url_decaissements_today_search_page+1+"/"+s);
            pages.setLast_page_url(url_decaissements_today_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_today_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_today_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(decaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/decaissements/date/page/{date}/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DecaissementPage selectAllPageDate(@PathVariable(value = "page") int page,
                                              @PathVariable(value = "date") String date) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Decaissements> decaissements = this.decaissementDao.findByDateDecaissementAndDeletedFalse(Helpers.getDateFromString(date), pageable);

        DecaissementPage pages = new DecaissementPage();

        Long total = this.decaissementDao.countDecaissements(Helpers.getDateFromString(date));
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
            pages.setFirst_page_url(url_decaissements_date_page+date+"/"+1);
            pages.setLast_page_url(url_decaissements_date_page+date+"/"+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_date_page+date+"/"+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_date_page+date+"/"+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(decaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/decaissements/date/search/page/{date}/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DecaissementPage searchDecaissementPageDate(@PathVariable(value = "page") int page,
                                                       @PathVariable(value = "date") String date,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Decaissements> decaissements = this.decaissementDao.recherche(Helpers.getDateFromString(date), s, pageable);

        DecaissementPage pages = new DecaissementPage();
        Long total = this.decaissementDao.countRecherche(Helpers.getDateFromString(date), s);
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
            pages.setFirst_page_url(url_decaissements_date_search_page+date+"/"+1+"/"+s);
            pages.setLast_page_url(url_decaissements_date_search_page+date+"/"+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_date_search_page+date+"/"+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_date_search_page+date+"/"+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(decaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/decaissements/caisse/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DecaissementPage selectAllCaissePage(@PathVariable(value = "page") int page,
                                                @CurrentUser final UserDetailsImpl currentUser) {

        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Decaissements> decaissements = this.decaissementDao.selectByCaisse(ligneCaisse.getCaissePK().getCaisse(), pageable);

        DecaissementPage pages = new DecaissementPage();

        Long total = this.decaissementDao.countByCaisse(ligneCaisse.getCaissePK().getCaisse());
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
            pages.setFirst_page_url(url_decaissements_caisse_page+1);
            pages.setLast_page_url(url_decaissements_caisse_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_caisse_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_caisse_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(decaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/decaissements/caisse/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DecaissementPage searchDecaissementCaissePage(@PathVariable(value = "page") int page,
                                                         @CurrentUser final UserDetailsImpl currentUser,
                                             @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Decaissements> decaissements = this.decaissementDao.rechercheCaisse(ligneCaisse.getCaissePK().getCaisse(), s, pageable);

        DecaissementPage pages = new DecaissementPage();
        Long total = this.decaissementDao.countRechercheCaisse(ligneCaisse.getCaissePK().getCaisse(), s);
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
            pages.setFirst_page_url(url_decaissements_caisse_search_page+1+"/"+s);
            pages.setLast_page_url(url_decaissements_caisse_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_caisse_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_caisse_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(decaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/decaissements/caisse/today/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DecaissementPage selectAllPageCaisseToday(@PathVariable(value = "page") int page,
                                         @CurrentUser final UserDetailsImpl currentUser) {

        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Decaissements> decaissements = this.decaissementDao.selectByCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(Helpers.currentDate()), pageable);

        DecaissementPage pages = new DecaissementPage();

        Long total = this.decaissementDao.countByCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(Helpers.currentDate()));
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
            pages.setFirst_page_url(url_decaissements_caisse_today_page+1);
            pages.setLast_page_url(url_decaissements_caisse_today_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_caisse_today_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_caisse_today_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(decaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/decaissements/caisse/today/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DecaissementPage searchDecaissementPageCaisseToday(@PathVariable(value = "page") int page,
                                                        @CurrentUser final UserDetailsImpl currentUser,
                                                        @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Decaissements> decaissements = this.decaissementDao.rechercheCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(Helpers.currentDate()), s, pageable);

        DecaissementPage pages = new DecaissementPage();
        Long total = this.decaissementDao.countRechercheCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(Helpers.currentDate()), s);
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
            pages.setFirst_page_url(url_decaissements_caisse_today_search_page+1+"/"+s);
            pages.setLast_page_url(url_decaissements_caisse_today_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_caisse_today_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_caisse_today_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(decaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/decaissements/caisse/date/page/{date}/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DecaissementPage selectAllPageCaisseDate(@PathVariable(value = "page") int page,
                                                    @CurrentUser final UserDetailsImpl currentUser,
                                              @PathVariable(value = "date") String date) {

        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Decaissements> decaissements = this.decaissementDao.selectByCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(date), pageable);

        DecaissementPage pages = new DecaissementPage();

        Long total = this.decaissementDao.countByCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(date));
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
            pages.setFirst_page_url(url_decaissements_caisse_date_page+date+"/"+1);
            pages.setLast_page_url(url_decaissements_caisse_date_page+date+"/"+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_caisse_date_page+date+"/"+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_caisse_date_page+date+"/"+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(decaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/decaissements/caisse/date/search/page/{date}/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DecaissementPage searchDecaissementPageCaisseDate(@PathVariable(value = "page") int page,
                                                       @PathVariable(value = "date") String date,
                                                             @CurrentUser final UserDetailsImpl currentUser,
                                                       @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Decaissements> decaissements = this.decaissementDao.rechercheCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(date), s, pageable);

        DecaissementPage pages = new DecaissementPage();
        Long total = this.decaissementDao.countRechercheCaisse(ligneCaisse.getCaissePK().getCaisse(), Helpers.getDateFromString(date), s);
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
            pages.setFirst_page_url(url_decaissements_caisse_date_search_page+date+"/"+1+"/"+s);
            pages.setLast_page_url(url_decaissements_caisse_date_search_page+date+"/"+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_decaissements_caisse_date_search_page+date+"/"+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_decaissements_caisse_date_search_page+date+"/"+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(decaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/decaissement", method =  RequestMethod.POST)
    public Decaissements save(@Valid @RequestBody Decaissements request,
                                         @CurrentUser final UserDetailsImpl currentUser) {
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);
        request.setMotif(request.getMotif());
        request.setMontant(request.getMontant());
        request.setDateDecaissement(new Date());
        request.setType(request.getType());
        request.setLigneCaisse(ligneCaisse);
        return this.decaissementDao.save(request);
    }

    @RequestMapping(value = "/decaissement/{id}", method =  RequestMethod.PUT)
    public Decaissements update(@Valid @RequestBody Decaissements request, @PathVariable("id") final Long id) {
        Decaissements decaissementInit = this.decaissementDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        decaissementInit.setMotif(request.getMotif());
        decaissementInit.setMontant(request.getMontant());
        decaissementInit.setDateDecaissement(new Date());
        decaissementInit.setType(request.getType());
        return this.decaissementDao.save(decaissementInit);
    }

    @RequestMapping(value = { "/decaissement/montant/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantDate(@PathVariable("date") final String date) {
        return this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(date));
    }
    @RequestMapping(value = { "/decaissement/montant/today" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantDate() {
        return this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(Helpers.currentDate()));
    }
    @RequestMapping(value = { "/decaissement/montant" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montant() {
        return this.decaissementDao.montantTotalDecaissements();
    }
    @RequestMapping(value = { "/decaissement/caisse/montant" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montant(@CurrentUser final UserDetailsImpl currentUser) {
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);
        return this.decaissementDao.montantByCaisse(ligneCaisse.getCaissePK().getCaisse());
    }
    @RequestMapping(value = { "/decaissement/caisse/montant/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantCaisseDate(@PathVariable("date") final String date) {
        return this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(date));
    }
    @RequestMapping(value = { "/decaissement/caisse/montant/today" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantCaisseDate() {
        return this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(Helpers.currentDate()));
    }

    @RequestMapping(value = "/decaissement/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Decaissements decaissementInit = this.decaissementDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        decaissementInit.setDeleted(true);
        this.decaissementDao.save(decaissementInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
