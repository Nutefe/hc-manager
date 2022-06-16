// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.*;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.Traitements;
import com.bluerizon.hcmanager.models.TypePatients;
import com.bluerizon.hcmanager.payload.pages.PatientPage;
import com.bluerizon.hcmanager.payload.pages.TraitementPage;
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
public class TraitementController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_traitement_page}")
    private String url_traitement_page;

    @Value("${app.url_traitement_search_page}")
    private String url_traitement_search_page;

    @Autowired
    private TraitementsDao traitementsDao;
    @Autowired
    private UtilisateursDao utilisateursDao;
    @Autowired
    private TypeTraitementsDao typeTraitementsDao;
    @Autowired
    private TypePatientsDao typePatientsDao;

    @GetMapping("/traitement/{id}")
    public Traitements getOne(@PathVariable("id") final Integer id) {
        final Traitements traitement = this.traitementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return traitement;
    }

    @GetMapping("/traitements")
    public List<Traitements> getAll() {
        List<Traitements> traitements= this.traitementsDao.findByDeletedFalse();
        return traitements;
    }

    @GetMapping("/traitements/type/patient/{id}")
    public List<Traitements> getAll(@PathVariable("id") final Integer id) {
        TypePatients patient = this.typePatientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        List<Traitements> traitements= this.traitementsDao.findByTypePatientAndDeletedFalse(patient);
        return traitements;
    }


    @RequestMapping(value ="/traitements/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public TraitementPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Traitements> traitements = this.traitementsDao.findByDeletedFalse(pageable);

        TraitementPage pages = new TraitementPage();

        Long total = this.traitementsDao.countTraitements();
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
            pages.setFirst_page_url(url_traitement_page+1);
            pages.setLast_page_url(url_traitement_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_traitement_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_traitement_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(traitements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/traitements/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TraitementPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Traitements> traitements = this.traitementsDao.recherche(s, pageable);

        TraitementPage pages = new TraitementPage();
        Long total = this.traitementsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_traitement_search_page+1+"/"+s);
            pages.setLast_page_url(url_traitement_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_traitement_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_traitement_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(traitements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/traitement", method =  RequestMethod.POST)
    public Traitements save(@Valid @RequestBody Traitements request, @CurrentUser final UserDetailsImpl currentUser) {
        request.setTypeTraitement(this.typeTraitementsDao.findById(request.getTypeTraitement().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        request.setUtilisateur(this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        request.setLibelle(request.getLibelle().toUpperCase());
        return this.traitementsDao.save(request);
    }

    @RequestMapping(value = "/traitement/{id}", method =  RequestMethod.PUT)
    public Traitements update(@Valid @RequestBody Traitements request, @PathVariable("id") final Integer id) {
        Traitements traitementInit = this.traitementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        traitementInit.setTypeTraitement(this.typeTraitementsDao.findById(request.getTypeTraitement().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        traitementInit.setLibelle(request.getLibelle().toUpperCase());
        traitementInit.setDescription(request.getDescription());
        traitementInit.setPrice(request.getPrice());
        return this.traitementsDao.save(traitementInit);
    }

    @RequestMapping(value = "/traitement/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        Traitements traitementInit = this.traitementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        traitementInit.setDeleted(true);
        this.traitementsDao.save(traitementInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
