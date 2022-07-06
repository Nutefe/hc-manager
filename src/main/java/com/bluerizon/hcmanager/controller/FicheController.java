// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.AssurancesDao;
import com.bluerizon.hcmanager.dao.FicheTraitementsDao;
import com.bluerizon.hcmanager.dao.FichesDao;
import com.bluerizon.hcmanager.dao.PatientsDao;
import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.pages.AssurancePage;
import com.bluerizon.hcmanager.payload.pages.FichePage;
import com.bluerizon.hcmanager.payload.response.FicheResponse;
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
public class FicheController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_fiche_page}")
    private String url_fiche_page;

    @Value("${app.url_fiche_search_page}")
    private String url_fiche_search_page;

    @Autowired
    private FichesDao fichesDao;
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private FicheTraitementsDao ficheTraitementsDao;

    @GetMapping("/fiche/{id}")
    public Fiches getOne(@PathVariable("id") final Long id) {
        final Fiches fiche = this.fichesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return fiche;
    }

    @GetMapping("/fiche/traitement/{id}")
    public List<FicheTraitements> getFicheTraitement(@PathVariable("id") final Long id) {
        final Fiches fiche = this.fichesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return ficheTraitementsDao.findByFiche(fiche);
    }

    @RequestMapping(value ="/fiches/page/{id}/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FichePage selectAllPage(@PathVariable(value = "id") Long id, @PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        Patients patient = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        List<FicheResponse> fiches = this.fichesDao.findByPatientAndDeletedFalseRes(patient, pageable);

        FichePage pages = new FichePage();

        Long total = this.fichesDao.countFiches(patient);
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
            pages.setFirst_page_url(url_fiche_page+id+"/"+1);
            pages.setLast_page_url(url_fiche_page+id+"/"+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_fiche_page+id+"/"+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_fiche_page+id+"/"+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(fiches);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/fiches/search/page/{id}/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FichePage searchFichePage(@PathVariable(value = "id") Long id, @PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        Patients patient = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        List<FicheResponse> fiches = this.fichesDao.rechercheRes(patient, s, pageable);

        FichePage pages = new FichePage();
        Long total = this.fichesDao.countRecherche(patient, s);
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
            pages.setFirst_page_url(url_fiche_search_page+id+"/"+1+"/"+s);
            pages.setLast_page_url(url_fiche_search_page+id+"/"+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_fiche_search_page+id+"/"+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_fiche_search_page+id+"/"+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(fiches);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
