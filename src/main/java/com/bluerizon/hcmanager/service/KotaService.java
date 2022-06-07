package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.KotasDao;
import com.bluerizon.hcmanager.models.Kotas;
import com.bluerizon.hcmanager.repository.KotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KotaService implements KotasDao {

    @Autowired
    private KotasRepository repository;

    @Override
    public Optional<Kotas> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Kotas findByLibelleAndDeletedFalse(String libelle) {
        return repository.findByLibelleAndDeletedFalse(libelle);
    }

    @Override
    public Kotas save(Kotas kota) {
        return repository.save(kota);
    }

    @Override
    public List<Kotas> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Kotas> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Kotas> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countKotas() {
        return repository.countKotas();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public Boolean existsByLibelle(String libelle) {
        return repository.existsByLibelle(libelle);
    }
}
