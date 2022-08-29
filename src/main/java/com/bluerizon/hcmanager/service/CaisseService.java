package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.CaisseDao;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.repository.CaissesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaisseService implements CaisseDao {

    @Autowired
    private CaissesRepository repository;

    @Override
    public Optional<Caisses> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Caisses save(Caisses caisse) {
        return repository.save(caisse);
    }

    @Override
    public List<Caisses> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Caisses> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Caisses> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countReserves() {
        return repository.countReserves();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }
}
