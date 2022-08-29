// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.CaisseDao;
import com.bluerizon.hcmanager.dao.LigneCaisseDao;
import com.bluerizon.hcmanager.dao.ReserveDao;
import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.models.CaissePK;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.LigneCaisses;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.payload.pages.CaissePage;
import com.bluerizon.hcmanager.payload.pages.ReservePage;
import com.bluerizon.hcmanager.payload.request.CaisseRequest;
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
public class CaisseController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_caisses_page}")
    private String url_caisses_page;

    @Value("${app.url_caisses_search_page}")
    private String url_caisses_search_page;

    @Autowired
    private CaisseDao caisseDao;
    @Autowired
    private LigneCaisseDao ligneCaisseDao;
    @Autowired
    private UtilisateursDao utilisateursDao;

    @GetMapping("/caisse/{id}")
    public Caisses getOne(@PathVariable("id") final Integer id) {
        final Caisses caisse = this.caisseDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return caisse;
    }

    @GetMapping("/caisses")
    public List<Caisses> getAll() {
        List<Caisses> caisses= this.caisseDao.findByDeletedFalse();
        return caisses;
    }



    @RequestMapping(value ="/caisses/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public CaissePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Caisses> caisses = this.caisseDao.findByDeletedFalse(pageable);

        CaissePage pages = new CaissePage();

        Long total = this.caisseDao.countReserves();
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
            pages.setFirst_page_url(url_caisses_page+1);
            pages.setLast_page_url(url_caisses_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_caisses_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_caisses_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(caisses);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/caisses/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CaissePage searchProfilPage(@PathVariable(value = "page") int page,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Caisses> caisses = this.caisseDao.recherche(s, pageable);

        CaissePage pages = new CaissePage();
        Long total = this.caisseDao.countRecherche(s);
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
            pages.setFirst_page_url(url_caisses_search_page+1+"/"+s);
            pages.setLast_page_url(url_caisses_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_caisses_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_caisses_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(caisses);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/ligne/caisse", method =  RequestMethod.POST)
    public Caisses save(@Valid @RequestBody CaisseRequest request) {
        Caisses caisse = new Caisses();
        caisse.setLibelle(request.getLibelle().toUpperCase());
        caisse.setMontant(request.getMontant());
        Caisses caisseSave = this.caisseDao.save(caisse);
        LigneCaisses ligne = new LigneCaisses();
        ligne.setCaissePK(new CaissePK(caisseSave, this.utilisateursDao.findById(request.getUtilisateur().getId())
                .orElseThrow(() -> new RuntimeException("Error: object is not found."))));
        this.ligneCaisseDao.save(ligne);
        return caisseSave;
    }

    @RequestMapping(value = "/caisse", method =  RequestMethod.POST)
    public Caisses save(@Valid @RequestBody Caisses request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        request.setMontant(request.getMontant());
        return this.caisseDao.save(request);
    }

    @RequestMapping(value = "/caisse/{id}", method =  RequestMethod.PUT)
    public Caisses update(@Valid @RequestBody Caisses request, @PathVariable("id") final Integer id) {
        Caisses caisseInit = this.caisseDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        caisseInit.setLibelle(request.getLibelle().toUpperCase());
        caisseInit.setMontant(request.getMontant());
        return this.caisseDao.save(caisseInit);
    }

    @RequestMapping(value = "/caisse/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        Caisses caisseInit = caisseDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        caisseInit.setDeleted(true);
        this.caisseDao.save(caisseInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
