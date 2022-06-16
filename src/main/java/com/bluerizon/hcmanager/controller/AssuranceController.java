// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.AssurancesDao;
import com.bluerizon.hcmanager.dao.ProfilsDao;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.payload.pages.AssurancePage;
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
public class AssuranceController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_assurance_page}")
    private String url_assurance_page;

    @Value("${app.url_assurance_search_page}")
    private String url_assurance_search_page;

    @Autowired
    private AssurancesDao assurancesDao;

    @GetMapping("/assurance/{id}")
    public Assurances getOne(@PathVariable("id") final Integer id) {
        final Assurances assurance = this.assurancesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));

        return assurance;
    }

    @GetMapping("/assurances")
    public List<Assurances> getAll() {
        List<Assurances> assurances= assurancesDao.findByDeletedFalse();
        return assurances;
    }


    @RequestMapping(value ="/assurances/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public AssurancePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Assurances> assurances = this.assurancesDao.findByDeletedFalse(pageable);

        AssurancePage pages = new AssurancePage();

        Long total = this.assurancesDao.countAssurances();
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
            pages.setFirst_page_url(url_assurance_page+1);
            pages.setLast_page_url(url_assurance_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_assurance_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_assurance_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(assurances);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/assurances/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public AssurancePage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Assurances> assurances = this.assurancesDao.recherche(s, pageable);

        AssurancePage pages = new AssurancePage();
        Long total = this.assurancesDao.countRecherche(s);
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
            pages.setFirst_page_url(url_assurance_search_page+1+"/"+s);
            pages.setLast_page_url(url_assurance_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_assurance_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_assurance_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(assurances);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/assurance", method =  RequestMethod.POST)
    public Assurances save(@Valid @RequestBody Assurances request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        return assurancesDao.save(request);
    }

    @RequestMapping(value = "/assurance/{id}", method =  RequestMethod.PUT)
    public Assurances update(@Valid @RequestBody Assurances request, @PathVariable("id") final Integer id) {
        Assurances assuranceInit = assurancesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        assuranceInit.setLibelle(request.getLibelle().toUpperCase());
        return assurancesDao.save(assuranceInit);
    }

    @RequestMapping(value = "/assurance/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        Assurances assuranceInit = assurancesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        assuranceInit.setDeleted(true);
        this.assurancesDao.save(assuranceInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
