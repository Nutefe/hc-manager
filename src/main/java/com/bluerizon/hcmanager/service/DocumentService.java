package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.DocumentsDao;
import com.bluerizon.hcmanager.models.Documents;
import com.bluerizon.hcmanager.repository.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService implements DocumentsDao {

    @Autowired
    private DocumentsRepository repository;

    @Override
    public Optional<Documents> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Documents save(Documents document) {
        return repository.save(document);
    }

    @Override
    public List<Documents> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Documents> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Documents> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countDocuments() {
        return repository.countDocuments();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }
}
