// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.AssurancesDao;
import com.bluerizon.hcmanager.dao.KotasDao;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Kotas;
import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.payload.pages.AssurancePage;
import com.bluerizon.hcmanager.payload.pages.KotaPage;
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
public class KotaController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_kota_page}")
    private String url_kota_page;

    @Value("${app.url_kota_search_page}")
    private String url_kota_search_page;

    @Autowired
    private KotasDao kotasDao;

    @GetMapping("/kota/{id}")
    public Kotas getOne(@PathVariable("id") final Integer id) {
        final Kotas kota = this.kotasDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return kota;
    }

    @GetMapping("/kotas")
    public List<Kotas> getAll() {
        List<Kotas> kotas= this.kotasDao.findByDeletedFalse();
        return kotas;
    }


    @RequestMapping(value ="/kotas/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public KotaPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Kotas> kotas = this.kotasDao.findByDeletedFalse(pageable);

        KotaPage pages = new KotaPage();

        Long total = this.kotasDao.countKotas();
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
            pages.setFirst_page_url(url_kota_page+1);
            pages.setLast_page_url(url_kota_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_kota_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_kota_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(kotas);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/kotas/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public KotaPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Kotas> kotas = this.kotasDao.recherche(s, pageable);

        KotaPage pages = new KotaPage();
        Long total = this.kotasDao.countRecherche(s);
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
            pages.setFirst_page_url(url_kota_search_page+1+"/"+s);
            pages.setLast_page_url(url_kota_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_kota_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_kota_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(kotas);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/kota", method =  RequestMethod.POST)
    public Kotas save(@Valid @RequestBody Kotas request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        return this.kotasDao.save(request);
    }

    @RequestMapping(value = "/kota/{id}", method =  RequestMethod.PUT)
    public Kotas update(@Valid @RequestBody Kotas request, @PathVariable("id") final Integer id) {
        Kotas kotaInit = this.kotasDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        kotaInit.setLibelle(request.getLibelle().toUpperCase());
        return this.kotasDao.save(kotaInit);
    }

    @RequestMapping(value = "/kota/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        Kotas kotaInit = this.kotasDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        kotaInit.setDeleted(true);
        this.kotasDao.save(kotaInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
