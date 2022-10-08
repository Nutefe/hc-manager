// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.*;
import com.bluerizon.hcmanager.exception.NotFoundRequestException;
import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.genarate.GenarateEtatCaisse;
import com.bluerizon.hcmanager.payload.genarate.GenarateFacture;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.EncaissementPage;
import com.bluerizon.hcmanager.payload.pages.EntreprisePage;
import com.bluerizon.hcmanager.payload.pages.FacturePage;
import com.bluerizon.hcmanager.payload.response.EtatEncaissemnt;
import com.bluerizon.hcmanager.payload.response.EtatRecette;
import com.bluerizon.hcmanager.security.jwt.CurrentUser;
import com.bluerizon.hcmanager.security.services.UserDetailsImpl;
import com.bluerizon.hcmanager.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EncaissementController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_encaissement_page}")
    private String url_encaissement_page;

    @Value("${app.url_encaissement_search_page}")
    private String url_encaissement_search_page;

    @Value("${app.url_encaissement_day_page}")
    private String url_encaissement_day_page;

    @Value("${app.url_encaissement_day_search_page}")
    private String url_encaissement_day_search_page;

    @Value("${app.url_encaissement_etat_page}")
    private String url_encaissement_etat_page;

    @Value("${app.url_encaissement_etat_search_page}")
    private String url_encaissement_etat_search_page;

    @Autowired
    private EncaissementsDao encaissementsDao;
    @Autowired
    private FacturesDao facturesDao;
    @Autowired
    private UtilisateursDao utilisateursDao;
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private LigneCaisseDao ligneCaisseDao;
    @Autowired
    private CaisseDao caisseDao;
    @Autowired
    private DecaissementDao decaissementDao;
    @Autowired
    private ReserveDao reserveDao;
    @Autowired
    private DepenseReserveDao depenseReserveDao;
    private final Logger logger;
    @Autowired
    private StorageService fileStorageService;
    public EncaissementController() {
        this.logger = LoggerFactory.getLogger((Class)this.getClass());
    }

    @GetMapping("/encaissement/{id}")
    public Encaissements getOne(@PathVariable("id") final Long id) {
        final Encaissements encaissement = this.encaissementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return encaissement;
    }

    @GetMapping("/encaissements")
    public List<Encaissements> getAll() {
        final List<Encaissements> encaissements= this.encaissementsDao.findByDeletedFalse();
        return encaissements;
    }

    @RequestMapping(value ="/encaissements/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public EncaissementPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Encaissements> encaissements = this.encaissementsDao.findByDeletedFalse(pageable);

        EncaissementPage pages = new EncaissementPage();

        Long total = this.encaissementsDao.countEncaissements();
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
            pages.setFirst_page_url(url_encaissement_page+1);
            pages.setLast_page_url(url_encaissement_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(encaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/encaissements/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EncaissementPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Encaissements> encaissements = this.encaissementsDao.recherche(s, pageable);

        EncaissementPage pages = new EncaissementPage();
        Long total = this.encaissementsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_encaissement_search_page+1+"/"+s);
            pages.setLast_page_url(url_encaissement_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(encaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value ="/encaissements/day/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public EncaissementPage selectAllDayPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Encaissements> encaissements = this.encaissementsDao.findByDateEncaissementAndDeletedFalse(Helpers.getDateFromString(Helpers.currentDate()), pageable);

        EncaissementPage pages = new EncaissementPage();

        Long total = this.encaissementsDao.countEncaissements(Helpers.getDateFromString(Helpers.currentDate()));
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
            pages.setFirst_page_url(url_encaissement_day_page+1);
            pages.setLast_page_url(url_encaissement_day_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_day_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_day_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(encaissements);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/encaissements/day/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EncaissementPage searchEncDayPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Encaissements> encaissements = this.encaissementsDao.recherche(Helpers.getDateFromString(Helpers.currentDate()), s, pageable);

        EncaissementPage pages = new EncaissementPage();
        Long total = this.encaissementsDao.countRecherche(Helpers.getDateFromString(Helpers.currentDate()), s);
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
            pages.setFirst_page_url(url_encaissement_day_search_page+1+"/"+s);
            pages.setLast_page_url(url_encaissement_day_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_day_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_day_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(encaissements);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }
    @RequestMapping(value ="/encaissements/facture/day/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public FacturePage selectAllFactureDayPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.encaissementsDao.selectFactureEncaisse(Helpers.getDateFromString(Helpers.currentDate()), pageable);

        FacturePage pages = new FacturePage();

        Long total = this.encaissementsDao.countEncaissements(Helpers.getDateFromString(Helpers.currentDate()));
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
            pages.setFirst_page_url(url_encaissement_day_page+1);
            pages.setLast_page_url(url_encaissement_day_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_day_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_day_page+(page-1));
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

    @RequestMapping(value = "/encaissement/facture/day/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FacturePage searchFactureEncDayPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.encaissementsDao.rechercheFacture(Helpers.getDateFromString(Helpers.currentDate()), s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.encaissementsDao.countRechercheFacture(Helpers.getDateFromString(Helpers.currentDate()), s);
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
            pages.setFirst_page_url(url_encaissement_day_search_page+1+"/"+s);
            pages.setLast_page_url(url_encaissement_day_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_day_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_day_search_page+(page-1)+"/"+s);
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

    @RequestMapping(value = "/encaissement", method =  RequestMethod.POST)
    public ResponseEntity<Resource> save(@Valid @RequestBody Encaissements request,
                                         @CurrentUser final UserDetailsImpl currentUser,
                                         final HttpServletRequest requestServlet) throws IOException {

        Factures facture = this.facturesDao.findById(request.getFacture().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);
        Encaissements paye = null;
        if (!facture.isSolde()){

            if (facture.isEncaisse()){
                List<Encaissements> encaissements = this.encaissementsDao.findByFactureAndDeletedFalse(facture);
                Double initEnc = 0.0;
                for (Encaissements item :
                        encaissements) {
                    initEnc +=item.getMontant();
                }
                if ((facture.getTotal() - initEnc) >= request.getMontant()){

                    Caisses caisse = ligneCaisse.getCaissePK().getCaisse();
                    caisse.setSolde(caisse.getSolde() + request.getMontant());
                    caisse.setRecette(caisse.getRecette() + request.getMontant());
                    this.caisseDao.save(caisse);

                    request.setLigneCaisse(ligneCaisse);
                    request.setFacture(facture);
                    request.setUtilisateur(utilisateur);
                    request.setDateEncaissement(new Date());
                    request.setReliquat(request.getTotal() - request.getMontant());
                    request.setFileName(Helpers.generatRef("Encaissement", this.encaissementsDao.count()+1)+".pdf");
                    request.setReste((facture.getTotal() - facture.getRemise()) - (initEnc + request.getMontant()));

                    paye = this.encaissementsDao.save(request);
                    facture.setEncaisse(true);
                    facture.setReste(facture.getTotal() - (initEnc + paye.getMontant() + facture.getRemise()));
                    Factures factureUp =this.facturesDao.save(facture);
                    if ((factureUp.getTotal() - factureUp.getRemise()) == (initEnc + paye.getMontant())){
                        factureUp.setSolde(true);
                        this.facturesDao.save(factureUp);
                    }
                } else {

                }
            } else {
                if (facture.getAcompte() > 0){
                    if (facture.getAcompte().compareTo(request.getMontant()) == 0){

                        Caisses caisse = ligneCaisse.getCaissePK().getCaisse();
                        caisse.setSolde(caisse.getSolde() + request.getMontant());
                        caisse.setRecette(caisse.getRecette() + request.getMontant());
                        this.caisseDao.save(caisse);

                        request.setLigneCaisse(ligneCaisse);
                        request.setFacture(facture);
                        request.setUtilisateur(utilisateur);
                        request.setDateEncaissement(new Date());
                        request.setReliquat(request.getTotal() - request.getMontant());
                        request.setFileName(Helpers.generatRef("Encaissement", this.encaissementsDao.count()+1)+".pdf");
                        request.setReste((facture.getTotal() - facture.getRemise()) - request.getMontant());

                        paye = this.encaissementsDao.save(request);
                        facture.setEncaisse(true);
                        facture.setReste(facture.getTotal() - (paye.getMontant() +  facture.getRemise()));
                        facture.setAcompte(0.0);
                        Factures factureUp = this.facturesDao.save(facture);
                        if ((factureUp.getTotal() - factureUp.getRemise()) == paye.getMontant()){
                            factureUp.setSolde(true);
                            this.facturesDao.save(factureUp);
                        }
                    } else {

                    }
                } else {
                    if ((facture.getTotal() - facture.getRemise()) >= request.getMontant()){

                        Caisses caisse = ligneCaisse.getCaissePK().getCaisse();
                        caisse.setSolde(caisse.getSolde() + request.getMontant());
                        caisse.setRecette(caisse.getRecette() + request.getMontant());
                        this.caisseDao.save(caisse);

                        request.setLigneCaisse(ligneCaisse);
                        request.setFacture(facture);
                        request.setUtilisateur(utilisateur);
                        request.setDateEncaissement(new Date());
                        request.setReliquat(request.getTotal() - request.getMontant());
                        request.setFileName(Helpers.generatRef("Encaissement", this.encaissementsDao.count()+1)+".pdf");
                        request.setReste((facture.getTotal() - facture.getRemise()) - request.getMontant());
                        paye = this.encaissementsDao.save(request);
                        facture.setEncaisse(true);
                        facture.setReste(facture.getTotal() - (paye.getMontant() +  facture.getRemise()));
                        Factures factureUp = this.facturesDao.save(facture);
                        if ((factureUp.getTotal() - factureUp.getRemise()) == paye.getMontant()){
                            factureUp.setSolde(true);
                            this.facturesDao.save(factureUp);
                        }
                    } else {

                    }
                }

            }
        }
//        Encaissements encaissementSave =  this.encaissementsDao.save(request);
        File bis = GenarateFacture.caisse(paye);

        final Resource resource = this.fileStorageService.loadAsResource(bis.getName());
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = requestServlet.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentLength(bis.length()) //
                .body(resource);
    }

    @RequestMapping(value = "/encaissement/count/day", method =  RequestMethod.GET)
    public Long countDay() {
        return this.encaissementsDao.countEncaissements(Helpers.getDateFromString(Helpers.currentDate()));
    }
    @RequestMapping(value = "/encaissement/montant/day", method =  RequestMethod.GET)
    public Double montantDay() {
        return this.encaissementsDao.montantDate(Helpers.getDateFromString(Helpers.currentDate())) -
                (this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(Helpers.currentDate())) +
                this.reserveDao.montantDateReserves(Helpers.getDateFromString(Helpers.currentDate())));
    }
    @RequestMapping(value = "/encaissement/facture/day", method =  RequestMethod.GET)
    public Long countFactureDay() {
        return this.encaissementsDao.countFacture(Helpers.getDateFromString(Helpers.currentDate()));
    }
    @RequestMapping(value = "/encaissement/patient/day", method =  RequestMethod.GET)
    public Long countPatientDay() {
        return this.encaissementsDao.countPatient(Helpers.getDateFromString(Helpers.currentDate()));
    }

    @RequestMapping(value = { "/encaissement/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Long count(@PathVariable("date") final String date) {
        return this.encaissementsDao.countEncaissements(Helpers.getDateFromString(date));
    }
    @RequestMapping(value = { "/encaissement/facture/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Long countFactureDate(@PathVariable("date") final String date) {
        return this.encaissementsDao.countFacture(Helpers.getDateFromString(date));
    }
    @RequestMapping(value = { "/encaissement/patient/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Long countPatientDate(@PathVariable("date") final String date) {
        return this.encaissementsDao.countPatient(Helpers.getDateFromString(date));
    }
    @RequestMapping(value = { "/encaissement/montant/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantDate(@PathVariable("date") final String date) {
        return this.encaissementsDao.montantDate(Helpers.getDateFromString(date));
    }

    @RequestMapping(value = { "/encaissement/montant" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Double montantTotal() {
        Double enc = this.encaissementsDao.montant() -
                (this.decaissementDao.montantTotalDecaissements() + this.reserveDao.montantTotalReserves());
        return enc;
    }

    @RequestMapping(value = { "/encaissement/etat/date/{date}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public void etatDate(@PathVariable("date") final String date) {
        this.encaissementsDao.countEncaissements(Helpers.getDateFromString(date));
        this.encaissementsDao.countFacture(Helpers.getDateFromString(date));
        this.encaissementsDao.montantDate(Helpers.getDateFromString(date));
        this.patientsDao.countDate(date);
    }


    @RequestMapping(value ="/encaissements/etat/date/page/{page}/{date}", method = RequestMethod.GET)
    @ResponseBody
    public EtatEncaissemnt etatDatePage(@PathVariable(value = "page") int page,
                                        @PathVariable("date") final String date) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Factures> factures = this.encaissementsDao.selectFactureEncaisse(Helpers.getDateFromString(date), pageable);

//        System.out.println(factures);

        FacturePage pages = new FacturePage();

        Long total = this.encaissementsDao.countEncaissements(Helpers.getDateFromString(date));
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
            pages.setFirst_page_url(url_encaissement_etat_page+1+"/"+date);
            pages.setLast_page_url(url_encaissement_etat_page+lastPage+"/"+date);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_etat_page+(page+1)+"/"+date);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_etat_page+(page-1)+"/"+date);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(factures);
        }else {
            pages.setTotal(0L);
        }
        EtatEncaissemnt etatEncaissemnt = new EtatEncaissemnt();
        etatEncaissemnt.setMontant(this.encaissementsDao.montantDate(Helpers.getDateFromString(date)));
        etatEncaissemnt.setEncaisse(this.encaissementsDao.countEncaissements(Helpers.getDateFromString(date)));
        etatEncaissemnt.setFacture(this.facturesDao.countDate(Helpers.getDateFromString(date)));
        etatEncaissemnt.setPatient(this.patientsDao.countDate(date));
        etatEncaissemnt.setPage(pages);
        return etatEncaissemnt;
    }

    @RequestMapping(value = "/encaissement/etat/date/search/page/{page}/{date}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EtatEncaissemnt searchEtatDatePage(@PathVariable(value = "page") int page,
                                          @PathVariable("date") final String date,
                                          @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Factures> factures = this.encaissementsDao.rechercheFacture(Helpers.getDateFromString(date), s, pageable);

        FacturePage pages = new FacturePage();
        Long total = this.encaissementsDao.countRechercheFacture(Helpers.getDateFromString(date), s);
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
            pages.setFirst_page_url(url_encaissement_etat_search_page+1+"/"+date+"/"+s);
            pages.setLast_page_url(url_encaissement_etat_search_page+lastPage+"/"+date+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_encaissement_etat_search_page+(page+1)+"/"+date+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_encaissement_etat_search_page+(page-1)+"/"+date+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(factures);

        }else {
            pages.setTotal(0L);
        }
        EtatEncaissemnt etatEncaissemnt = new EtatEncaissemnt();
        etatEncaissemnt.setMontant(this.encaissementsDao.montantDate(Helpers.getDateFromString(date)));
        etatEncaissemnt.setEncaisse(this.encaissementsDao.countEncaissements(Helpers.getDateFromString(date)));
        etatEncaissemnt.setFacture(this.facturesDao.countDate(Helpers.getDateFromString(date)));
        etatEncaissemnt.setPatient(this.patientsDao.countDate(date));
        etatEncaissemnt.setPage(pages);

        return etatEncaissemnt;
    }

    @RequestMapping(value = "/recette/etat/date/{start}/{end}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EtatRecette selectEtatDate(@PathVariable(value = "start") String dateStart,
                                      @PathVariable("end") final String dateEnd){

        EtatRecette etatRecette = new EtatRecette();
        etatRecette.setMontantRecette(this.encaissementsDao.montantDateEncaissement(Helpers.getDateFromString(dateStart), Helpers.getDateFromString(dateEnd)));
        etatRecette.setMontantDecaissement(this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(dateStart), Helpers.getDateFromString(dateEnd)));
        etatRecette.setMontantReserve(this.reserveDao.montantDateReserve(Helpers.getDateFromString(dateStart), Helpers.getDateFromString(dateEnd)));
        etatRecette.setMontantDepense(this.depenseReserveDao.montantDateDepense(Helpers.getDateFromString(dateStart), Helpers.getDateFromString(dateEnd)));

        return etatRecette;
    }


    @RequestMapping(value = "/etat/caisse", method =  RequestMethod.GET)
    public ResponseEntity<Resource> saveProforma(@CurrentUser final UserDetailsImpl currentUser,
                                                 final HttpServletRequest requestServlet) throws IOException {
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        LigneCaisses ligneCaisse = this.ligneCaisseDao.findFirstByUser(utilisateur);

        File bis = GenarateEtatCaisse.etatCaisse(ligneCaisse.getCaissePK().getCaisse());

        final Resource resource = this.fileStorageService.loadAsResource(bis.getName());
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = requestServlet.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentLength(bis.length()) //
                .body(resource);
    }

    @RequestMapping(value = "/encaissement/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Encaissements encaissementInit = this.encaissementsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        encaissementInit.setDeleted(true);
        this.encaissementsDao.save(encaissementInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
