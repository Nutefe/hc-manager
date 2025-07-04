// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.DocumentsDao;
import com.bluerizon.hcmanager.dao.TypeTraitementsDao;
import com.bluerizon.hcmanager.models.Documents;
import com.bluerizon.hcmanager.models.TypeTraitements;
import com.bluerizon.hcmanager.payload.pages.DocumentPage;
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
public class DocumentController
{
    @Value("${app.base}")
    private String path;

    @Value("${app.page_size}")
    private Integer page_size;

    @Value("${app.url_document_page}")
    private String url_document_page;

    @Value("${app.url_document_search_page}")
    private String url_document_search_page;
    @Autowired
    private DocumentsDao documentsDao;

    @GetMapping("/document/{id}")
    public Documents getOne(@PathVariable("id") final Integer id) {
        final Documents document = this.documentsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        return document;
    }

    @GetMapping("/documents")
    public List<Documents> getAll() {
        List<Documents> documents= this.documentsDao.findByDeletedFalse();
        return documents;
    }


    @RequestMapping(value ="/documents/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public DocumentPage selectAllPage(@PathVariable(value = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());

        List<Documents> documents = this.documentsDao.findByDeletedFalse(pageable);

        DocumentPage pages = new DocumentPage();

        Long total = this.documentsDao.countDocuments();
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
            pages.setFirst_page_url(url_document_page+1);
            pages.setLast_page_url(url_document_page+lastPage);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_document_page+(page+1));
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_document_page+(page-1));
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }
            pages.setPath(path);
            pages.setData(documents);
        }else {
            pages.setTotal(0L);
        }

        return pages;
    }

    @RequestMapping(value = "/documents/search/page/{page}/{s}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DocumentPage searchProfilPage(@PathVariable(value = "page") int page,
                                                   @PathVariable(value = "s") String s){
        if (s.contains("-")){
            s = s.replaceAll("-", "/");
        } else if (s.contains("&&")) {
            s = s.replaceAll("&&", "-");
        }
        Pageable pageable = PageRequest.of(page - 1, page_size, sortByCreatedDesc());
        List<Documents> documents = this.documentsDao.recherche(s, pageable);

        DocumentPage pages = new DocumentPage();
        Long total = this.documentsDao.countRecherche(s);
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
            pages.setFirst_page_url(url_document_search_page+1+"/"+s);
            pages.setLast_page_url(url_document_search_page+lastPage+"/"+s);
            if (page >= lastPage){

            }else {
                pages.setNext_page_url(url_document_search_page+(page+1)+"/"+s);
            }

            if (page == 1){
                pages.setPrev_page_url(null);
                pages.setFrom(1L);
                pages.setTo(Long.valueOf(page_size));
            } else {
                pages.setPrev_page_url(url_document_search_page+(page-1)+"/"+s);
                pages.setFrom(1L + (Long.valueOf(page_size)*(page -1)));
                pages.setTo(Long.valueOf(page_size) * page);
            }

            pages.setPath(path);
            pages.setData(documents);

        }else {
            pages.setTotal(0L);
        }

        return pages;
    }


    @RequestMapping(value = "/document", method =  RequestMethod.POST)
    public Documents save(@Valid @RequestBody Documents request) {
        request.setLibelle(request.getLibelle().toUpperCase());
        return this.documentsDao.save(request);
    }

    @RequestMapping(value = "/document/{id}", method =  RequestMethod.PUT)
    public Documents update(@Valid @RequestBody Documents request, @PathVariable("id") final Integer id) {
        Documents documentInit = this.documentsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        documentInit.setLibelle(request.getLibelle().toUpperCase());
        return this.documentsDao.save(documentInit);
    }

    @RequestMapping(value = "/document/{id}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable("id") final Integer id) {
        Documents documentInit = this.documentsDao.findById(id).orElseThrow(() -> new RuntimeException("Error: object is not found."));
        documentInit.setDeleted(true);
        this.documentsDao.save(documentInit);
    }
    
    private Sort sortByCreatedDesc() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }
}
