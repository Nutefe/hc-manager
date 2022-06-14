package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.TraitementsDao;
import com.bluerizon.hcmanager.models.Traitements;
import com.bluerizon.hcmanager.models.TypePatients;
import com.bluerizon.hcmanager.repository.TraitementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraitementService implements TraitementsDao {

    @Autowired
    private TraitementsRepository repository;

    @Override
    public Optional<Traitements> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Traitements save(Traitements traitement) {
        return repository.save(traitement);
    }

    @Override
    public List<Traitements> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Traitements> findByTypePatientAndDeletedFalse(TypePatients typePatient) {
        return repository.findByTypePatientAndDeletedFalse(typePatient);
    }

    @Override
    public List<Traitements> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Traitements> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countTraitements() {
        return repository.countTraitements();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }
}
