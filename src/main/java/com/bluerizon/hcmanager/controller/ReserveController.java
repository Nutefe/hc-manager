// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.AssurancesDao;
import com.bluerizon.hcmanager.dao.ReserveDao;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.payload.pages.AssurancePage;
import com.bluerizon.hcmanager.payload.pages.ReservePage;
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
public class ReserveController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_reserves_page}")
    private String url_reserves_page;

    @Value("${app.url_reserves_search_page}")
    private String url_reserves_search_page;

    @Autowired
    private ReserveDao reserveDao;

    @GetMapping("/reserve/{id}")
    public Reserves getOne(@PathVariable("id") final Integer id) {
        final Reserves reserve = this.reserveDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return reserve;
    }

    @GetMapping("/reserves")
    public List<Reserves> getAll() {
        List<Reserves> reserves= this.reserveDao.findByDeletedFalse();
        return reserves;
    }

    @RequestMapping(value ="/reserves/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public ReservePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Reserves> reserves = this.reserveDao.findByDeletedFalse(pageable);

        ReservePage pages = new ReservePage();

        Long total = this.reserveDao.countReserves();
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
            pages.setFirst_page_url(url_reserves_page+1);
            pages.setLast_page_url(url_reserves_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_reserves_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_reserves_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(reserves);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/reserves/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ReservePage searchProfilPage(@PathVariable(value = "page") int page,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Reserves> reserves = this.reserveDao.recherche(s, pageable);

        ReservePage pages = new ReservePage();
        Long total = this.reserveDao.countRecherche(s);
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
            pages.setFirst_page_url(url_reserves_search_page+1+"/"+s);
            pages.setLast_page_url(url_reserves_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_reserves_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_reserves_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(reserves);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/reserve", method =  RequestMethod.POST)
    public Reserves save(@Valid @RequestBody Reserves request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        request.setMontantSuivant(request.getMontantDefini());
        request.setMontantReserve(0.0);
        return this.reserveDao.save(request);
    }

    @RequestMapping(value = "/reserve/{id}", method =  RequestMethod.PUT)
    public Reserves update(@Valid @RequestBody Reserves request, @PathVariable("id") final Integer id) {
        Reserves reserveInit = this.reserveDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        reserveInit.setLibelle(request.getLibelle().toUpperCase());
        reserveInit.setHeure(request.getHeure());
        reserveInit.setJours(reserveInit.getJours());
        if (reserveInit.getMontantDefini() >= request.getMontantDefini())
            reserveInit.setMontantSuivant(reserveInit.getMontantSuivant() - (reserveInit.getMontantDefini() - request.getMontantDefini()));
        else
            reserveInit.setMontantSuivant(reserveInit.getMontantSuivant() + (request.getMontantDefini() - reserveInit.getMontantDefini()));
        reserveInit.setMontantDefini(request.getMontantDefini());
        return this.reserveDao.save(reserveInit);
    }

    @RequestMapping(value = "/reserve/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        Reserves reserveInit = reserveDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        reserveInit.setDeleted(true);
        this.reserveDao.save(reserveInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
