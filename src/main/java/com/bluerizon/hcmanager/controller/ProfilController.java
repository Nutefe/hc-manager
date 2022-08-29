// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.ProfilsDao;
import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.payload.pages.ProfilPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfilController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_profil_all_page}")
    private String url_profil_all_page;

    @Value("${app.url_profil_all_search_page}")
    private String url_profil_all_search_page;

    @Autowired
    private ProfilsDao profilsDao;

    @GetMapping("/profil/{id}")
    public Profils getOne(@PathVariable("id") final Integer id) {
        final Profils profil = this.profilsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: Profil is not found."));

        return profil;
    }

    @GetMapping("/profils")
    public List<Profils> getAll() {
        List<Profils> profils= profilsDao.findByDeletedFalse();
        return profils;
    }


    @RequestMapping(value ="/profils/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public ProfilPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Profils> profils = this.profilsDao.selectProfils(pageable);

        ProfilPage pages = new ProfilPage();

        Long total = this.profilsDao.countProfils();
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
            pages.setFirst_page_url(url_profil_all_page+1);
            pages.setLast_page_url(url_profil_all_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_profil_all_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_profil_all_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(profils);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/profils/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ProfilPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Profils> profils = this.profilsDao.recherche(s, pageable);

        ProfilPage pages = new ProfilPage();
        Long total = this.profilsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_profil_all_search_page+1+"/"+s);
            pages.setLast_page_url(url_profil_all_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_profil_all_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_profil_all_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(profils);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/profil", method =  RequestMethod.POST)
    public Profils save(@Valid @RequestBody Profils request) {
        Profils profil = new Profils();
        profil.setId(profilsDao.count().intValue()+1);
        profil.setLibelle(request.getLibelle());
        Profils profilInit = profilsDao.save(profil);

        return profilInit;
    }

    @RequestMapping(value = "/profil/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<?> update(@Valid @RequestBody Profils request, @PathVariable("id") final Integer id) {
        Profils profilInit = profilsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: Profil is not found."));
        profilInit.setLibelle(request.getLibelle());

        Profils profilSave = profilsDao.save(profilInit);
        return ResponseEntity.ok(profilSave);
    }

    @RequestMapping(value = "/profil/{id}", method =  RequestMethod.DELETE)
    public void update(@PathVariable("id") final Integer id) {
        Profils profilInit = profilsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: Profil is not found."));

        profilInit.setDeleted(true);
        profilsDao.save(profilInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
