// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.*;
import com.bluerizon.hcmanager.exception.BadRequestException;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.AssurancePage;
import com.bluerizon.hcmanager.payload.pages.ReservePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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
    @Autowired
    private CaisseDao caisseDao;
    @Autowired
    private EncaissementsDao encaissementsDao;
    @Autowired
    private DecaissementDao decaissementDao;
    @Autowired
    private DepenseReserveDao depenseReserveDao;

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
        request.setCaisse(caisseDao.findById(request.getCaisse().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        request.setLibelle(request.getLibelle().toUpperCase());
        request.setMontantSuivant(request.getMontantDefini());
        request.setMontantReserve(0.0);
        request.setDateReserve(new Date());
        request.setDateSuivant(Helpers.tomorowDate());
        return this.reserveDao.save(request);
    }
    @RequestMapping(value = "/reserve/journaliere", method =  RequestMethod.GET)
    public ResponseEntity<?> save() {
        Reserves reserveInit = this.reserveDao.findTop1ByDeletedFalse();
        Caisses caisse = this.caisseDao.findById(reserveInit.getCaisse().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Reserves reserve = new Reserves();
        reserve.setCaisse(caisse);
        reserve.setLibelle(reserveInit.getLibelle().toUpperCase());
        reserve.setHeure(reserveInit.getHeure());
        reserve.setJours(reserveInit.getJours());
        reserve.setDateReserve(new Date());
        reserve.setDateSuivant(Helpers.tomorowDate());
        reserve.setMontantDefini(reserveInit.getMontantDefini());
//        Double enc = this.encaissementsDao.montantDate(Helpers.getDateFromString(Helpers.currentDate()));
//        Double dec = this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(Helpers.currentDate()));

        Long nd = Helpers.dayBetween(reserveInit.getDateSuivant(), new Date());

        if (nd == 0){
            if (caisse.getSolde() >= reserveInit.getMontantSuivant()){
                reserve.setMontantReserve(reserveInit.getMontantSuivant());
                reserve.setMontantSuivant(reserveInit.getMontantDefini());
                caisse.setDecaissement(caisse.getDecaissement() + reserveInit.getMontantSuivant());
                caisse.setSolde(caisse.getSolde() - reserveInit.getMontantSuivant());
                this.caisseDao.save(caisse);
            } else {
                reserve.setMontantReserve(caisse.getSolde());
                reserve.setMontantSuivant((reserveInit.getMontantSuivant() - caisse.getSolde()) + reserveInit.getMontantDefini());
                caisse.setDecaissement(caisse.getDecaissement() + caisse.getSolde());
                caisse.setSolde(caisse.getSolde() - caisse.getSolde());
                this.caisseDao.save(caisse);
            }
            return ResponseEntity.ok(this.reserveDao.save(reserve));
        } else if (nd > 0) {
            if (caisse.getSolde() >= (reserveInit.getMontantSuivant()*nd)){
                reserve.setMontantReserve(reserveInit.getMontantSuivant()*nd);
                reserve.setMontantSuivant(reserveInit.getMontantDefini());
                caisse.setDecaissement(caisse.getDecaissement() + (reserveInit.getMontantSuivant()*nd));
                caisse.setSolde(caisse.getSolde() - (reserveInit.getMontantSuivant()*nd));
                this.caisseDao.save(caisse);
            } else {
                reserve.setMontantReserve(caisse.getSolde());
                reserve.setMontantSuivant(((reserveInit.getMontantSuivant()*nd) - caisse.getSolde()) + reserveInit.getMontantDefini());
                caisse.setDecaissement(caisse.getDecaissement() + caisse.getSolde());
                caisse.setSolde(caisse.getSolde() - caisse.getSolde());
                this.caisseDao.save(caisse);
            }
            return ResponseEntity.ok(this.reserveDao.save(reserve));
        } else {
            return ResponseEntity.badRequest().body(new BadRequestException("Montant superieur au solde de la caisse"));
        }
    }

    @RequestMapping(value = "/reserve/jours", method =  RequestMethod.POST)
    public ResponseEntity<?> saveReserve(@Valid @RequestBody Reserves request) {
        Reserves reserveInit = this.reserveDao.findFirst1ByDeletedFalseOrderByIdDesc();
        Reserves reserveSecond = this.reserveDao.findTop1ByDeletedFalse();
        Caisses caisse = this.caisseDao.findById(reserveInit.getCaisse().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Reserves reserve = new Reserves();
        reserve.setCaisse(caisse);
        reserve.setLibelle(reserveInit.getLibelle().toUpperCase());
//        reserve.setHeure(reserveInit.getHeure());
//        reserve.setJours(reserveInit.getJours());
        reserve.setDateReserve(new Date());
        reserve.setDateSuivant(Helpers.tomorowDate());
        reserve.setMontantDefini(request.getMontantDefini());
//        System.out.println(reserveSecond.getMontantSuivant());
//        System.out.println(request.getMontantDefini());
        if (request.getMontantDefini() <= reserveSecond.getMontantSuivant()){
            if (request.getMontantDefini() == reserveSecond.getMontantSuivant()){
                System.out.println(reserveSecond.getMontantDefini());
                reserve.setMontantReserve(request.getMontantDefini());
                reserve.setMontantSuivant(reserveInit.getMontantDefini());
                caisse.setDecaissement(caisse.getDecaissement() + request.getMontantDefini());
                caisse.setSolde(caisse.getSolde() - request.getMontantDefini());
                this.caisseDao.save(caisse);
                return ResponseEntity.ok(this.reserveDao.save(reserve));
            } else{
                reserve.setMontantReserve(request.getMontantDefini());
                reserve.setMontantSuivant((reserveSecond.getMontantSuivant() - request.getMontantDefini()) + reserveInit.getMontantDefini());
                caisse.setDecaissement(caisse.getDecaissement() + request.getMontantDefini());
                caisse.setSolde(caisse.getSolde() - request.getMontantDefini());
                this.caisseDao.save(caisse);
                return ResponseEntity.ok(this.reserveDao.save(reserve));
            }
        }
        else {
            return ResponseEntity.badRequest().body(new BadRequestException("Montant superieur au solde de la caisse"));
        }

//        Double enc = this.encaissementsDao.montantDate(Helpers.getDateFromString(Helpers.currentDate()));
//        Double dec = this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(Helpers.currentDate()));

//        Long nd = Helpers.dayBetween(reserveInit.getDateSuivant(), new Date());

//        if (nd == 0){
//            if (caisse.getSolde() >= reserveInit.getMontantSuivant()){
//                reserve.setMontantReserve(reserveInit.getMontantSuivant());
//                reserve.setMontantSuivant(reserveInit.getMontantDefini());
//                caisse.setDecaissement(caisse.getDecaissement() + reserveInit.getMontantSuivant());
//                caisse.setSolde(caisse.getSolde() - reserveInit.getMontantSuivant());
//                this.caisseDao.save(caisse);
//            } else {
//                reserve.setMontantReserve(caisse.getSolde());
//                reserve.setMontantSuivant((reserveInit.getMontantSuivant() - caisse.getSolde()) + reserveInit.getMontantDefini());
//                caisse.setDecaissement(caisse.getDecaissement() + caisse.getSolde());
//                caisse.setSolde(caisse.getSolde() - caisse.getSolde());
//                this.caisseDao.save(caisse);
//            }
//            return ResponseEntity.ok(this.reserveDao.save(reserve));
//        } else if (nd > 0) {
//            if (caisse.getSolde() >= (reserveInit.getMontantSuivant()*nd)){
//                reserve.setMontantReserve(reserveInit.getMontantSuivant()*nd);
//                reserve.setMontantSuivant(reserveInit.getMontantDefini());
//                caisse.setDecaissement(caisse.getDecaissement() + (reserveInit.getMontantSuivant()*nd));
//                caisse.setSolde(caisse.getSolde() - (reserveInit.getMontantSuivant()*nd));
//                this.caisseDao.save(caisse);
//            } else {
//                reserve.setMontantReserve(caisse.getSolde());
//                reserve.setMontantSuivant(((reserveInit.getMontantSuivant()*nd) - caisse.getSolde()) + reserveInit.getMontantDefini());
//                caisse.setDecaissement(caisse.getDecaissement() + caisse.getSolde());
//                caisse.setSolde(caisse.getSolde() - caisse.getSolde());
//                this.caisseDao.save(caisse);
//            }
//            return ResponseEntity.ok(this.reserveDao.save(reserve));
//        } else {
//            return ResponseEntity.badRequest().body(new BadRequestException("Montant superieur au solde de la caisse"));
//        }
    }

    @RequestMapping(value = "/reserve/{id}", method =  RequestMethod.PUT)
    public Reserves update(@Valid @RequestBody Reserves request, @PathVariable("id") final Integer id) {
        Reserves reserveInit = this.reserveDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        reserveInit.setCaisse(caisseDao.findById(request.getCaisse().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        reserveInit.setLibelle(request.getLibelle().toUpperCase());
        reserveInit.setHeure(request.getHeure());
        reserveInit.setJours(reserveInit.getJours());
        reserveInit.setDateReserve(new Date());
        reserveInit.setDateSuivant(Helpers.tomorowDate());
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

    @RequestMapping(value = "/reserve/day", method =  RequestMethod.GET)
    public Double montantReserveDay() {
        return this.reserveDao.montantDateReserves(Helpers.getDateFromString(Helpers.currentDate()));
    }
    @RequestMapping(value = "/reserve/date/{date}", method =  RequestMethod.GET)
    public Double montantReserveDate(@PathVariable("date") final String date) {
        return this.reserveDao.montantDateReserves(Helpers.getDateFromString(date));
    }
    @RequestMapping(value = "/reserve/montant", method =  RequestMethod.GET)
    public Double montantReserve() {
        Double res = this.reserveDao.montantTotalReserves() - this.depenseReserveDao.montantTotalReserves();
        return res;
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
