// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.controller;

import com.bluerizon.hcmanager.dao.FacturesDao;
import com.bluerizon.hcmanager.dao.FicheTraitementsDao;
import com.bluerizon.hcmanager.dao.TraitementsDao;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Traitements;
import com.bluerizon.hcmanager.payload.genarate.GenarateFacture;
import com.bluerizon.hcmanager.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({ "/api" })
public class FileController
{
    private static final Logger logger;
    @Autowired
    private StorageService fileStorageService;
    @Autowired
    private FacturesDao facturesDao;
    @Autowired
    private FicheTraitementsDao traitementsDao;
    
    @PostMapping({ "/uploadFile" })
    public void uploadFile(@RequestParam("file") final MultipartFile file) {
        this.fileStorageService.storeFile(file);
    }
    
    @PostMapping({ "/uploadMultiFile" })
    public void uploadMultiFile(@RequestParam("file") final MultipartFile[] file) {
        for (int i = 0; i < file.length; ++i) {
            this.fileStorageService.storeFile(file[i]);
        }
    }
    
    @GetMapping({ "/downloadFile/facture/{fileName:.+}" })
    public ResponseEntity<Resource> downloadFileFacture(@PathVariable final String fileName, final HttpServletRequest request)throws IOException {
        Resource resource = null;
        if (fileStorageService.existFile(fileName)){
            resource = fileStorageService.loadAsResource(fileName);
        } else {
            if (facturesDao.existsByFileName(fileName)){
                Factures facture = this.facturesDao.findByFileName(fileName);
                List<FicheTraitements> traitements = this.traitementsDao.findByFiche(facture.getFiche());
                File bis = null;
                if (facture.getFiche().getPatient().getTypePatient().getId() == 1){
                    bis = GenarateFacture.inamPdf(facture, traitements);
                } else if (facture.getFiche().getPatient().getTypePatient().getId() == 2) {
                    bis = GenarateFacture.autrePdf(facture, traitements);
                } else if (facture.getFiche().getPatient().getTypePatient().getId() == 3) {
                    bis = GenarateFacture.nonPdf(facture, traitements);
                }
                resource = this.fileStorageService.loadAsResource(bis.getName());
            }
        }

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
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
                .body(resource);
    }

    @GetMapping({ "/downloadFile/{fileName:.+}" })
    public ResponseEntity<Resource> downloadFile(@PathVariable final String fileName, final HttpServletRequest request)throws IOException {
        Resource resource = fileStorageService.loadAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
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
                .body(resource);
    }
    
    @GetMapping({ "/downloadAvatar/{fileName:.+}" })
    public ResponseEntity<Resource> downloadAvatar(@PathVariable final String fileName, final HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
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
                .body(resource);
    }
    
    @RequestMapping(value = { "/files/{file_name}" }, method = { RequestMethod.DELETE })
    public String delete(@PathVariable("file_name") final String file_name) {
        if (this.fileStorageService.deleteOne(file_name)) {
            return "Supprimer avec success";
        }
        return "erreur de suppression";
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)FileController.class);
    }
}
