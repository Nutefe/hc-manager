// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.*;
import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.genarate.GenarateEtat;
import com.bluerizon.hcmanager.payload.genarate.GenarateFacture;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.pages.EncaissementPage;
import com.bluerizon.hcmanager.payload.pages.EtatPage;
import com.bluerizon.hcmanager.payload.request.EtatRequest;
import com.bluerizon.hcmanager.payload.response.EtatFactureEntreprise;
import com.bluerizon.hcmanager.payload.response.EtatFacturePatient;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bluerizon.hcmanager.payload.helper.Helpers.getDateFromString;

@RestController
@RequestMapping("/api")
public class EtatController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;
    @Value("${app.url_etat_page}")
    private String url_etat_page;
    @Value("${app.url_etat_search_page}")
    private String url_etat_search_page;

    @Autowired
    private EtatsDao etatsDao;
    @Autowired
    private FacturesDao facturesDao;
    @Autowired
    private UtilisateursDao utilisateursDao;
    @Autowired
    private AssurancesDao assurancesDao;
    @Autowired
    private FicheTraitementsDao ficheTraitementsDao;
    private final Logger logger;
    @Autowired
    private StorageService fileStorageService;
    public EtatController() {
        this.logger = LoggerFactory.getLogger((Class)this.getClass());
    }

    @GetMapping("/etat/{id}")
    public Etats getOne(@PathVariable("id") final Long id) {
        final Etats etat = this.etatsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return etat;
    }

    @GetMapping("/etats")
    public List<Etats> getAll() {
        final List<Etats> etats= this.etatsDao.findByDeletedFalse();
        return etats;
    }

    @RequestMapping(value ="/etats/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public EtatPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Etats> etats = this.etatsDao.findByDeletedFalse(pageable);

        EtatPage pages = new EtatPage();

        Long total = this.etatsDao.countEtats();
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
            pages.setFirst_page_url(url_etat_page+1);
            pages.setLast_page_url(url_etat_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_etat_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_etat_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(etats);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/etats/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EtatPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Etats> etats = this.etatsDao.recherche(s, pageable);

        EtatPage pages = new EtatPage();
        Long total = this.etatsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_etat_search_page+1+"/"+s);
            pages.setLast_page_url(url_etat_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_etat_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_etat_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(etats);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/etat/patient", method =  RequestMethod.POST)
    public ResponseEntity<Resource> savePatient(@Valid @RequestBody EtatRequest request,
                                         @CurrentUser final UserDetailsImpl currentUser,
                                         final HttpServletRequest requestServlet) throws IOException {

        Assurances assurance = this.assurancesDao.findById(request.getAssurance().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Etats etat = new Etats();
        etat.setAssurance(assurance);
        etat.setUtilisateur(utilisateur);
        etat.setType(request.getType());
        etat.setDateEtat(new Date());
        etat.setDateDebut(getDateFromString(request.getStart()));
        etat.setDateFin(getDateFromString(request.getEnd()));
        etat.setFileName(Helpers.generatRef("Etats", this.etatsDao.count()+1)+".pdf");
        Etats etatSave = this.etatsDao.save(etat);

        List<EtatFacturePatient> facturePatients = new ArrayList<>();
        List<Factures> factures = this.facturesDao.etatFacture(assurance,
                getDateFromString(request.getStart()),
                getDateFromString(request.getEnd()));

        for (Factures item :
                factures) {
            EtatFacturePatient etatFacture = new EtatFacturePatient();
            List<FicheTraitements> traitements = this.ficheTraitementsDao.findByFiche(item.getFiche());
            etatFacture.setFacture(item);
            etatFacture.setFiches(traitements);
            etatFacture.setTypes(this.ficheTraitementsDao.findByFicheTraitement(item.getFiche()));
            facturePatients.add(etatFacture);
        }

        File bis = GenarateEtat.etatPatient(facturePatients, etatSave);

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

    @RequestMapping(value = "/etat/entreprise", method =  RequestMethod.POST)
    public ResponseEntity<Resource> saveEntreprise(@Valid @RequestBody EtatRequest request,
                                         @CurrentUser final UserDetailsImpl currentUser,
                                         final HttpServletRequest requestServlet) throws IOException {

        Assurances assurance = this.assurancesDao.findById(request.getAssurance().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Utilisateurs utilisateur = this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        Etats etat = new Etats();
        etat.setAssurance(assurance);
        etat.setUtilisateur(utilisateur);
        etat.setType(request.getType());
        etat.setDateEtat(new Date());
        etat.setDateDebut(getDateFromString(request.getStart()));
        etat.setDateFin(getDateFromString(request.getEnd()));
        etat.setFileName(Helpers.generatRef("Etats", this.etatsDao.count()+1)+".pdf");
        Etats etatSave = this.etatsDao.save(etat);

        List<Entreprises> entreprises = this.facturesDao.etatEntreprise(assurance,
                getDateFromString(request.getStart()),
                getDateFromString(request.getEnd()));

        List<EtatFactureEntreprise> factureEntreprises = new ArrayList<>();

        List<EtatFacturePatient> facturePatients = new ArrayList<>();
        for (Entreprises item :
                entreprises) {
            EtatFactureEntreprise factureEntreprise= new EtatFactureEntreprise();

            List<Factures> factures = this.facturesDao.etatEntreprise(
                    item,
                    assurance,
                    getDateFromString(request.getStart()),
                    getDateFromString(request.getEnd()));

            for (Factures facture :
                    factures) {
                EtatFacturePatient etatFacture = new EtatFacturePatient();
                List<FicheTraitements> traitements = this.ficheTraitementsDao.findByFiche(facture.getFiche());
                etatFacture.setFacture(facture);
                etatFacture.setFiches(traitements);
                etatFacture.setTypes(this.ficheTraitementsDao.findByFicheTraitement(facture.getFiche()));
                facturePatients.add(etatFacture);

            }
            factureEntreprise.setEntreprise(item);
            factureEntreprise.setFacturePatients(facturePatients);
            factureEntreprises.add(factureEntreprise);
        }

        File bis = GenarateEtat.etatEntreprise(factureEntreprises, etatSave);

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

    @RequestMapping(value = "/etats/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Etats etatInit = this.etatsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        etatInit.setDeleted(true);
        this.etatsDao.save(etatInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
