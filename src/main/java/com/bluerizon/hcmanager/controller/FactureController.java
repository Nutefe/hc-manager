// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.*;
import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.genarate.GenarateFacture;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.EncaissementPage;
import com.bluerizon.hcmanager.payload.pages.FacturePage;
import com.bluerizon.hcmanager.payload.request.FactureRequest;
import com.bluerizon.hcmanager.payload.request.TraitementRequest;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FactureController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_facture_page}")
    private String url_facture_page;

    @Value("${app.url_facture_search_page}")
    private String url_facture_search_page;

    @Value("${app.url_facture_solde_page}")
    private String url_facture_solde_page;

    @Value("${app.url_facture_solde_search_page}")
    private String url_facture_solde_search_page;

    @Value("${app.url_facture_enc_page}")
    private String url_facture_enc_page;

    @Value("${app.url_facture_enc_search_page}")
    private String url_facture_enc_search_page;

    @Autowired
    private FacturesDao facturesDao;
    @Autowired
    private UtilisateursDao utilisateursDao;
    @Autowired
    private FichesDao fichesDao;
    @Autowired
    private TraitementsDao traitementsDao;
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private KotasDao kotasDao;

    @Autowired
    private FicheTraitementsDao ficheTraitementsDao;

    @GetMapping("/facture/{id}")
    public Factures getOne(@PathVariable("id") final Long id) {
        final Factures facture = this.facturesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return facture;
    }

    @GetMapping("/factures")
    public List<Factures> getAll() {
        final List<Factures> factures= this.facturesDao.findByDeletedFalse();
        return factures;
    }


    @RequestMapping(value ="/factures/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FacturePage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.facturesDao.findByDeletedFalse(pageable);

        FacturePage pages = new FacturePage();

        Long total = this.facturesDao.countFactures();
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
            pages.setFirst_page_url(url_facture_page+1);
            pages.setLast_page_url(url_facture_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(factures);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/factures/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FacturePage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.facturesDao.recherche(s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.facturesDao.countRecherche(s);
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
            pages.setFirst_page_url(url_facture_search_page+1+"/"+s);
            pages.setLast_page_url(url_facture_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(factures);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/factures/solde/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FacturePage selectAllSoldePage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.facturesDao.findByDeletedFalseAndSoldeTrue(pageable);

        FacturePage pages = new FacturePage();

        Long total = this.facturesDao.countFacturesSolde();
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
            pages.setFirst_page_url(url_facture_solde_page+1);
            pages.setLast_page_url(url_facture_solde_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_solde_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_solde_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(factures);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/factures/solde/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FacturePage searchSoldePage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.facturesDao.rechercheSolde( s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.facturesDao.countRechercheSolde( s);
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
            pages.setFirst_page_url(url_facture_solde_search_page+1+"/"+s);
            pages.setLast_page_url(url_facture_solde_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_solde_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_solde_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(factures);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/factures/enc/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FacturePage selectAllEncPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.facturesDao.findByDeletedFalseAndEncaisseTrue( pageable);

        FacturePage pages = new FacturePage();

        Long total = this.facturesDao.countFacturesEncaisse();
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
            pages.setFirst_page_url(url_facture_enc_page+1);
            pages.setLast_page_url(url_facture_enc_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_enc_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_enc_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(factures);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/factures/enc/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FacturePage searchEncDayPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.facturesDao.rechercheEncaisse( s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.facturesDao.countRechercheEncaisse( s);
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
            pages.setFirst_page_url(url_facture_enc_search_page+1+"/"+s);
            pages.setLast_page_url(url_facture_enc_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_facture_enc_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_facture_enc_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(factures);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/facture", method =  RequestMethod.POST)
    public void save(@Valid @RequestBody FactureRequest request, @CurrentUser final UserDetailsImpl currentUser) throws IOException {

        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Patients patient = this.patientsDao.findById(request.getPatient().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Fiches fiche = new Fiches();
        fiche.setUtilisateur(utilisateur);
        fiche.setPatient(patient);
        fiche.setDateFiche(new Date());
        Fiches ficheSave = this.fichesDao.save(fiche);
        List<FicheTraitements> ficheTraitements = new ArrayList<>();
        Double total = 0.0;
        for (TraitementRequest item :
                request.getTraitements()) {

            FicheTraitements traitement = new FicheTraitements();
            Traitements traitementInit = this.traitementsDao.findById(item.getTraitement().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
            traitement.setFicheTraitementPK(new FicheTraitementPK(ficheSave, traitementInit));
            traitement.setBaseRembours(item.getBaseRembour());
            if (!item.getKota().trim().equals("") || item.getKota() != null){
                if (kotasDao.existsByLibelle(item.getKota())){
                    traitement.setKota(this.kotasDao.findByLibelleAndDeletedFalse(item.getKota()));
                }else {
                    Kotas kota = new Kotas();
                    kota.setLibelle(item.getKota());
                    Kotas kotaSave = this.kotasDao.save(kota);
                    traitement.setKota(kotaSave);
                }
            }
            traitement.setUnite(request.isUnite());
            if (traitementInit.getTypePatient().getId() == 1){
                if (request.isUnite() == false){
                    Double netAss = (traitementInit.getPrice() * item.getNetAssurance())/100;
                    traitement.setNetPayAssu(netAss);
                    traitement.setBaseRembours(item.getBaseRembour());
                    traitement.setNetPayBeneficiaire(traitementInit.getPrice() - netAss);
                } else {
                    traitement.setNetPayAssu(item.getNetAssurance());
                    traitement.setBaseRembours(item.getBaseRembour());
                    traitement.setNetPayBeneficiaire(traitementInit.getPrice() - item.getNetAssurance());
                }
            } else if (traitementInit.getTypePatient().getId() == 2) {
                if (request.isUnite() == false){
                    Double netAss = (traitementInit.getPrice() * item.getBaseRembour())/100;
                    traitement.setNetPayAssu(netAss);
                    traitement.setBaseRembours(item.getBaseRembour());
                    traitement.setNetPayBeneficiaire(traitementInit.getPrice() - netAss);
                } else {
                    traitement.setNetPayAssu(item.getBaseRembour());
                    traitement.setBaseRembours(item.getBaseRembour());
                    traitement.setNetPayBeneficiaire(traitementInit.getPrice() - item.getBaseRembour());
                }
            }
            total += traitement.getNetPayBeneficiaire();
            ficheTraitements.add(traitement);
        }
        List<FicheTraitements> traitementSave = this.ficheTraitementsDao.saveAll(ficheTraitements);
        Factures facture = new Factures();
        facture.setNumero(this.facturesDao.count()+11+"");
        facture.setFiche(ficheSave);
        facture.setUtilisateur(utilisateur);
        facture.setDateFacture(new Date());
        facture.setTotal(total);
        if (request.getAccompte() > (total - request.getRemise()))
            facture.setAcompte(0.0);
        else
            facture.setAcompte(request.getAccompte());
        facture.setRemise(request.getRemise());
        facture.setFileName(Helpers.generatRef("Facture", this.facturesDao.count()+1));
        facture.setReste(total - request.getRemise());
        Factures factureSave = this.facturesDao.save(facture);
        GenarateFacture.myPdfHelloWordClientMoral(factureSave, traitementSave);
        //return this.facturesDao.save(request);
    }

//    @RequestMapping(value = "/encaissement/{id}", method =  RequestMethod.PUT)
//    public Entreprises update(@Valid @RequestBody Entreprises request, @PathVariable("id") final Long id) {
//        Entreprises entrepriseInit = this.entreprisesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
//        entrepriseInit.setRaisonSocial(request.getRaisonSocial());
//        entrepriseInit.setNif(request.getNif());
//        entrepriseInit.setTelephone(request.getTelephone());
//        entrepriseInit.setAdresse(request.getAdresse());
//        return this.entreprisesDao.save(entrepriseInit);
//    }

    @RequestMapping(value = "/facture/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Factures factureInit = this.facturesDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        factureInit.setDeleted(true);
        this.facturesDao.save(factureInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
