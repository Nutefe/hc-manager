package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.EntreprisesDao;
import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.repository.EntreprisesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepriseService implements EntreprisesDao {

    @Autowired
    private EntreprisesRepository repository;

    @Override
    public Optional<Entreprises> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Entreprises save(Entreprises entreprise) {
        return repository.save(entreprise);
    }

    @Override
    public List<Entreprises> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Entreprises> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Entreprises> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countEntreprises() {
        return repository.countEntreprises();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public Boolean existsByRaisonSocial(String raisonSocial) {
        return repository.existsByRaisonSocial(raisonSocial);
    }

    @Override
    public Boolean existsByTelephone(String telephone) {
        return repository.existsByTelephone(telephone);
    }

    @Override
    public boolean existsByRaisonSocial(String raisonSocial, Long id) {
        return repository.existsByRaisonSocial(raisonSocial, id);
    }

    @Override
    public boolean existsByTelephone(String telephone, Long id) {
        return repository.existsByTelephone(telephone, id);
    }
}
