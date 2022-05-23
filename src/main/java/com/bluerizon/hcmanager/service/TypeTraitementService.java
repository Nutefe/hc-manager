package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.TypeTraitementsDao;
import com.bluerizon.hcmanager.models.TypePatients;
import com.bluerizon.hcmanager.models.TypeTraitements;
import com.bluerizon.hcmanager.repository.TypeTraitementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeTraitementService implements TypeTraitementsDao {

    @Autowired
    private TypeTraitementsRepository repository;

    @Override
    public Optional<TypeTraitements> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public TypeTraitements save(TypeTraitements traitement) {
        return repository.save(traitement);
    }

    @Override
    public List<TypeTraitements> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<TypeTraitements> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<TypeTraitements> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countTypeTraitements() {
        return repository.countTypeTraitements();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }
}
