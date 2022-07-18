package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.EtatsDao;
import com.bluerizon.hcmanager.models.Etats;
import com.bluerizon.hcmanager.repository.EtatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtatsService implements EtatsDao {

    @Autowired
    private EtatsRepository repository;

    @Override
    public Optional<Etats> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Etats save(Etats etat) {
        return repository.save(etat);
    }

    @Override
    public List<Etats> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Etats> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Etats> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Long countEtats() {
        return repository.countEtats();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }
}
