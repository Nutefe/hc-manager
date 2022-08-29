// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.TraitementsDao;
import com.bluerizon.hcmanager.dao.TypeTraitementsDao;
import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.models.Traitements;
import com.bluerizon.hcmanager.models.TypeTraitements;
import com.bluerizon.hcmanager.payload.pages.TraitementPage;
import com.bluerizon.hcmanager.payload.pages.TypeTraitementPage;
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
public class TypeTraitementController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_type_traitement_page}")
    private String url_type_traitement_page;

    @Value("${app.url_type_traitement_search_page}")
    private String url_type_traitement_search_page;
    @Autowired
    private TypeTraitementsDao typeTraitementsDao;

    @GetMapping("/type/traitement/{id}")
    public TypeTraitements getOne(@PathVariable("id") final Integer id) {
        final TypeTraitements traitement = this.typeTraitementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return traitement;
    }

    @GetMapping("/type/traitements")
    public List<TypeTraitements> getAll() {
        List<TypeTraitements> traitements= this.typeTraitementsDao.findByDeletedFalse();
        return traitements;
    }


    @RequestMapping(value ="/type/traitements/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public TypeTraitementPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<TypeTraitements> traitements = this.typeTraitementsDao.findByDeletedFalse(pageable);

        TypeTraitementPage pages = new TypeTraitementPage();

        Long total = this.typeTraitementsDao.countTypeTraitements();
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
            pages.setFirst_page_url(url_type_traitement_page+1);
            pages.setLast_page_url(url_type_traitement_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_type_traitement_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_type_traitement_page+(page-1));
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

    @RequestMapping(value = "/type/traitements/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TypeTraitementPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<TypeTraitements> traitements = this.typeTraitementsDao.recherche(s, pageable);

        TypeTraitementPage pages = new TypeTraitementPage();
        Long total = this.typeTraitementsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_type_traitement_search_page+1+"/"+s);
            pages.setLast_page_url(url_type_traitement_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_type_traitement_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_type_traitement_search_page+(page-1)+"/"+s);
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


    @RequestMapping(value = "/type/traitement", method =  RequestMethod.POST)
    public TypeTraitements save(@Valid @RequestBody TypeTraitements request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        return this.typeTraitementsDao.save(request);
    }

    @RequestMapping(value = "/type/traitement/{id}", method =  RequestMethod.PUT)
    public TypeTraitements update(@Valid @RequestBody TypeTraitements request, @PathVariable("id") final Integer id) {
        TypeTraitements traitementInit = this.typeTraitementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        traitementInit.setLibelle(request.getLibelle().toUpperCase());
        return this.typeTraitementsDao.save(traitementInit);
    }

    @RequestMapping(value = "/type/traitement/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        TypeTraitements traitementInit = this.typeTraitementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        traitementInit.setDeleted(true);
        this.typeTraitementsDao.save(traitementInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
