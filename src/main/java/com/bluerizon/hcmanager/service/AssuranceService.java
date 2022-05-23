package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.AssurancesDao;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.repository.AssurancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssuranceService implements AssurancesDao {

    @Autowired
    private AssurancesRepository repository;

    @Override
    public Optional<Assurances> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Assurances save(Assurances assurance) {
        return repository.save(assurance);
    }

    @Override
    public List<Assurances> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Assurances> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Assurances> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countAssurances() {
        return repository.countAssurances();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }
}
