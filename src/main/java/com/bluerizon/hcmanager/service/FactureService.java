package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.FacturesDao;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.repository.FacturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FactureService implements FacturesDao {

    @Autowired
    private FacturesRepository repository;

    @Override
    public Optional<Factures> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Factures save(Factures facture) {
        return repository.save(facture);
    }

    @Override
    public List<Factures> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Factures> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Factures> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countFactures() {
        return repository.countFactures();
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public List<Factures> findByDeletedFalseAndEncaisseTrue(Pageable pageable) {
        return repository.findByDeletedFalseAndEncaisseTrue(pageable);
    }

    @Override
    public List<Factures> rechercheEncaisse(String search, Pageable pageable) {
        return repository.rechercheEncaisse(search, pageable);
    }

    @Override
    public Long countFacturesEncaisse() {
        return repository.countFacturesEncaisse();
    }

    @Override
    public Long countRechercheEncaisse(String search) {
        return repository.countRechercheEncaisse(search);
    }

    @Override
    public List<Factures> findByDeletedFalseAndSoldeTrue(Pageable pageable) {
        return repository.findByDeletedFalseAndSoldeTrue(pageable);
    }

    @Override
    public List<Factures> rechercheSolde(String search, Pageable pageable) {
        return repository.rechercheSolde(search, pageable);
    }

    @Override
    public Long countFacturesSolde() {
        return repository.countFacturesSolde();
    }

    @Override
    public Long countRechercheSolde(String search) {
        return repository.countRechercheSolde(search);
    }

    @Override
    public List<Factures> findByDeletedFalseAndSoldeFalse(Pageable pageable) {
        return repository.findByDeletedFalseAndSoldeFalse(pageable);
    }

    @Override
    public List<Factures> rechercheSoldeFalse(String search, Pageable pageable) {
        return repository.rechercheSoldeFalse(search, pageable);
    }

    @Override
    public Long countFacturesSoldeFalse() {
        return repository.countFacturesSoldeFalse();
    }

    @Override
    public Long countRechercheSoldeFalse(String search) {
        return repository.countRechercheSoldeFalse(search);
    }
}
