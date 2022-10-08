// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.CaisseDao;
import com.bluerizon.hcmanager.dao.DepenseReserveDao;
import com.bluerizon.hcmanager.dao.ReserveDao;
import com.bluerizon.hcmanager.exception.BadRequestException;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.DepenseReserves;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.CaissePage;
import com.bluerizon.hcmanager.payload.pages.DepenseReservePage;
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
public class DepenseReserveController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_depense_reserves_page}")
    private String url_depense_reserves_page;

    @Value("${app.url_depense_reserves_search_page}")
    private String url_depense_reserves_search_page;
    @Autowired
    private DepenseReserveDao depenseReserveDao;
    @Autowired
    private ReserveDao reserveDao;

    @GetMapping("/depense/reserve/{id}")
    public DepenseReserves getOne(@PathVariable("id") final Long id) {
        final DepenseReserves depenseReserve = this.depenseReserveDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return depenseReserve;
    }

    @GetMapping("/depense/reserves")
    public List<DepenseReserves> getAll() {
        List<DepenseReserves> depenseReserves= this.depenseReserveDao.findByDeletedFalse();
        return depenseReserves;
    }

    @RequestMapping(value ="/depense/reserves/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DepenseReservePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<DepenseReserves> depenseReserves = this.depenseReserveDao.findByDeletedFalse(pageable);

        DepenseReservePage pages = new DepenseReservePage();

        Long total = this.depenseReserveDao.countReserves();
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
            pages.setFirst_page_url(url_depense_reserves_page+1);
            pages.setLast_page_url(url_depense_reserves_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_depense_reserves_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_depense_reserves_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(depenseReserves);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/depense/reserves/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DepenseReservePage searchProfilPage(@PathVariable(value = "page") int page,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<DepenseReserves> depenseReserves = this.depenseReserveDao.recherche(s, pageable);

        DepenseReservePage pages = new DepenseReservePage();
        Long total = this.depenseReserveDao.countRecherche(s);
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
            pages.setFirst_page_url(url_depense_reserves_search_page+1+"/"+s);
            pages.setLast_page_url(url_depense_reserves_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_depense_reserves_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_depense_reserves_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(depenseReserves);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/depense/reserve", method =  RequestMethod.POST)
    public ResponseEntity<?> save(@Valid @RequestBody DepenseReserves request) {
        Reserves reserveInit = this.reserveDao.findTop1ByDeletedFalse();
        Double reserve = this.reserveDao.montantTotalReserves();
        Double depense = this.depenseReserveDao.montantTotalReserves();
        if (request.getMontant() <= (reserve - depense)){
            request.setMotif(request.getMotif());
            request.setMontant(request.getMontant());
            request.setDateDepense(new Date());
            DepenseReserves depenseReserves = this.depenseReserveDao.save(request);
            if (request.isTotal()){
                List<Reserves> reserves = this.reserveDao.selectAllReserves();
                reserves.forEach( item -> {
                    item.setDeleted(true);
                    this.reserveDao.save(item);
                });
                List<DepenseReserves> depenseReservesDel = this.depenseReserveDao.findByDeletedFalse();
                depenseReservesDel.forEach( item ->{
                    item.setDeleted(true);
                    this.depenseReserveDao.save(item);
                });
            }
            return ResponseEntity.ok(depenseReserves);
        } else {
            return ResponseEntity.badRequest().body(new BadRequestException("Montant superieur a la reserve"));
        }
    }

    @RequestMapping(value = "/depense/reserve/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<?> update(@Valid @RequestBody DepenseReserves request, @PathVariable("id") final Long id) {
        DepenseReserves depenseReserveInit = this.depenseReserveDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Double reserve = this.reserveDao.montantTotalReserves();
        Double depense = this.depenseReserveDao.montantTotalReserves();
        if (request.getMontant() <= ((reserve + depenseReserveInit.getMontant()) - depense)){
            depenseReserveInit.setMotif(request.getMotif());
            depenseReserveInit.setMontant(request.getMontant());
            depenseReserveInit.setDateDepense(new Date());
            depenseReserveInit.setTotal(request.isTotal());
            DepenseReserves depenseReserves = this.depenseReserveDao.save(depenseReserveInit);
            if (request.isTotal()){
                Reserves reserveIn = this.reserveDao.findTop1ByDeletedFalse();
                if (reserveIn.isFinale()){
                    List<Reserves> reserves = this.reserveDao.selectAllReserves();
                    reserves.forEach( item -> {
                        if (item.isFinale()){
                            item.setDeleted(true);
                            this.reserveDao.save(item);
                        }
                    });
                    List<DepenseReserves> depenseReservesDel = this.depenseReserveDao.findByDeletedFalse();
                    depenseReservesDel.forEach( item ->{
                        item.setDeleted(true);
                        this.depenseReserveDao.save(item);
                    });
                }

            }
            return ResponseEntity.ok(depenseReserves);
        } else {
            return ResponseEntity.badRequest().body(new BadRequestException("Montant superieur a la reserve"));
        }
    }

    @RequestMapping(value = { "/depense/reserve/montant/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantDate(@PathVariable("date") final String date) {
        return this.depenseReserveDao.montantReserves(Helpers.getDateFromString(date));
    }
    @RequestMapping(value = { "/depense/reserve/montant/today" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantToday(@PathVariable("date") final String date) {
        return this.depenseReserveDao.montantReserves(Helpers.getDateFromString(Helpers.currentDate()));
    }
    @RequestMapping(value = { "/depense/reserve/montant" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montant() {
        return this.depenseReserveDao.montantTotalReserves();
    }

    @RequestMapping(value = "/depense/reserve/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        DepenseReserves depenseReserveInit = this.depenseReserveDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        depenseReserveInit.setDeleted(true);
        this.depenseReserveDao.save(depenseReserveInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
