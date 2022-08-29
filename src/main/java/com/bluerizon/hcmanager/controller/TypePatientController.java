// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.TypePatientsDao;
import com.bluerizon.hcmanager.dao.TypeTraitementsDao;
import com.bluerizon.hcmanager.models.TypePatients;
import com.bluerizon.hcmanager.models.TypeTraitements;
import com.bluerizon.hcmanager.payload.pages.TypePatientPage;
import com.bluerizon.hcmanager.payload.pages.TypeTraitementPage;
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
public class TypePatientController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_type_patient_page}")
    private String url_type_patient_page;

    @Value("${app.url_type_patient_search_page}")
    private String url_type_patient_search_page;
    @Autowired
    private TypePatientsDao patientsDao;

    @GetMapping("/type/patient/{id}")
    public TypePatients getOne(@PathVariable("id") final Integer id) {
        final TypePatients traitement = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return traitement;
    }

    @GetMapping("/type/patients")
    public List<TypePatients> getAll() {
        List<TypePatients> traitements= this.patientsDao.findByDeletedFalse();
        return traitements;
    }


    @RequestMapping(value ="/type/patients/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public TypePatientPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<TypePatients> patients = this.patientsDao.findByDeletedFalse(pageable);

        TypePatientPage pages = new TypePatientPage();

        Long total = this.patientsDao.countTypePatients();
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
            pages.setFirst_page_url(url_type_patient_page+1);
            pages.setLast_page_url(url_type_patient_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_type_patient_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_type_patient_page+(page-1));
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

    @RequestMapping(value = "/type/patients/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TypePatientPage searchProfilPage(@PathVariable(value = "page") int page,
                                            @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<TypePatients> patients = this.patientsDao.recherche(s, pageable);

        TypePatientPage pages = new TypePatientPage();
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
            pages.setFirst_page_url(url_type_patient_search_page+1+"/"+s);
            pages.setLast_page_url(url_type_patient_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_type_patient_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_type_patient_search_page+(page-1)+"/"+s);
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


    @RequestMapping(value = "/type/patient", method =  RequestMethod.POST)
    public TypePatients save(@Valid @RequestBody TypePatients request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        return this.patientsDao.save(request);
    }

    @RequestMapping(value = "/type/patient/{id}", method =  RequestMethod.PUT)
    public TypePatients update(@Valid @RequestBody TypeTraitements request, @PathVariable("id") final Integer id) {
        TypePatients patientInit = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        patientInit.setLibelle(request.getLibelle().toUpperCase());
        return this.patientsDao.save(patientInit);
    }

    @RequestMapping(value = "/type/patient/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        TypePatients patientInit = this.patientsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        patientInit.setDeleted(true);
        this.patientsDao.save(patientInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
