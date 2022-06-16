// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.*;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.payload.pages.AssurancePage;
import com.bluerizon.hcmanager.payload.pages.PatientPage;
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
public class PatientController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_patient_page}")
    private String url_patient_page;

    @Value("${app.url_patient_search_page}")
    private String url_patient_search_page;

    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private UtilisateursDao utilisateursDao;
    @Autowired
    private TypePatientsDao typePatientsDao;
    @Autowired
    private AssurancesDao assurancesDao;
    @Autowired
    private EntreprisesDao entreprisesDao;

    @GetMapping("/patient/{id}")
    public Patients getOne(@PathVariable("id") final Long id) {
        final Patients patient = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return patient;
    }

    @GetMapping("/patients")
    public List<Patients> getAll() {
        List<Patients> patients= this.patientsDao.findByDeletedFalse();
        return patients;
    }


    @RequestMapping(value ="/patients/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public PatientPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Patients> patients = this.patientsDao.findByDeletedFalse(pageable);

        PatientPage pages = new PatientPage();

        Long total = this.patientsDao.countPatients();
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
            pages.setFirst_page_url(url_patient_page+1);
            pages.setLast_page_url(url_patient_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_patient_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_patient_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(patients);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/patients/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PatientPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Patients> patients = this.patientsDao.recherche(s, pageable);

        PatientPage pages = new PatientPage();
        Long total = this.patientsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_patient_search_page+1+"/"+s);
            pages.setLast_page_url(url_patient_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_patient_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_patient_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(patients);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/patient", method =  RequestMethod.POST)
    public Patients save(@Valid @RequestBody Patients request, @CurrentUser final UserDetailsImpl currentUser) {
        System.out.println(request);
        if (request.getAssurance().getId() != null)
            request.setAssurance(this.assurancesDao.findById(request.getAssurance().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        else
            request.setAssurance(null);
        request.setTypePatient(this.typePatientsDao.findById(request.getTypePatient().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        request.setUtilisateur(this.utilisateursDao.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        if (request.getEntreprise().getId() != null)
            request.setEntreprise(this.entreprisesDao.findById(request.getEntreprise().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        else
            request.setEntreprise(null);
        request.setNom(request.getNom().toUpperCase());
        request.setGenre(request.getGenre().toUpperCase());
        return this.patientsDao.save(request);
    }

    @RequestMapping(value = "/patient/{id}", method =  RequestMethod.PUT)
    public Patients update(@Valid @RequestBody Patients request, @PathVariable("id") final Long id) {
        Patients patientInit = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        patientInit.setAssurance(this.assurancesDao.findById(request.getAssurance().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        patientInit.setTypePatient(this.typePatientsDao.findById(request.getTypePatient().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        patientInit.setEntreprise(this.entreprisesDao.findById(request.getEntreprise().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found.")));
        patientInit.setCodeDossier(request.getCodeDossier());
        patientInit.setNom(request.getNom().toUpperCase());
        patientInit.setPrenom(request.getPrenom());
        patientInit.setAdresse(request.getAdresse());
        patientInit.setDateNaiss(request.getDateNaiss());
        patientInit.setNumeroPiece(request.getNumeroPiece());
        patientInit.setGenre(request.getGenre().toUpperCase());
        patientInit.setPieceExp(request.getPieceExp());
        patientInit.setTelephone(request.getTelephone());
        return this.patientsDao.save(patientInit);
    }

    @RequestMapping(value = "/patient/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Long id) {
        Patients patientInit = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        patientInit.setDeleted(true);
        this.patientsDao.save(patientInit);
    }

    @RequestMapping(value = { "/check/code/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkCode(@PathVariable("s") final String s) {
        return this.patientsDao.existsByCodeDossier(s);
    }

    @RequestMapping(value = { "/check/code/update/{id}/{s}" }, method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkCodeId(@PathVariable("s") final String s, @PathVariable("id") final Long id) {
        return this.patientsDao.existsByCodeDossier(s, id);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
