package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.DecaissementDao;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Decaissements;
import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.repository.DecaissementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DecaissementService implements DecaissementDao {

    @Autowired
    private DecaissementsRepository repository;

    @Override
    public Optional<Decaissements> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Decaissements save(Decaissements decaissement) {
        return repository.save(decaissement);
    }

    @Override
    public List<Decaissements> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Decaissements> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Decaissements> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countDecaissements() {
        return repository.countDecaissements();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public List<Decaissements> findByDateDecaissementAndDeletedFalse(Date dateDecaissement, Pageable pageable) {
        return repository.findByDateDecaissementAndDeletedFalse(dateDecaissement, pageable);
    }

    @Override
    public List<Decaissements> recherche(Date dateDecaissement, String search, Pageable pageable) {
        return repository.recherche(dateDecaissement, search, pageable);
    }

    @Override
    public Long countDecaissements(Date dateDecaissement) {
        return repository.countDecaissements(dateDecaissement);
    }

    @Override
    public Long countRecherche(Date dateDecaissement, String search) {
        return repository.countRecherche(dateDecaissement, search);
    }

    @Override
    public List<Decaissements> selectByCaisse(Caisses caisse, Pageable pageable) {
        return repository.selectByCaisse(caisse, pageable);
    }

    @Override
    public List<Decaissements> rechercheCaisse(Caisses caisse, String search, Pageable pageable) {
        return repository.rechercheCaisse(caisse, search, pageable);
    }

    @Override
    public Long countByCaisse(Caisses caisse) {
        return repository.countByCaisse(caisse);
    }

    @Override
    public Long countRechercheCaisse(Caisses caisse, String search) {
        return repository.countRechercheCaisse(caisse, search);
    }

    @Override
    public List<Decaissements> selectByCaisse(Caisses caisse, Date dateDecaissement, Pageable pageable) {
        return repository.selectByCaisse(caisse, dateDecaissement, pageable);
    }

    @Override
    public List<Decaissements> rechercheCaisse(Caisses caisse, Date dateDecaissement, String search, Pageable pageable) {
        return repository.rechercheCaisse(caisse, dateDecaissement, search, pageable);
    }

    @Override
    public Long countByCaisse(Caisses caisse, Date dateDecaissement) {
        return repository.countByCaisse(caisse, dateDecaissement);
    }

    @Override
    public Long countRechercheCaisse(Caisses caisse, Date dateDecaissement, String search) {
        return repository.countRechercheCaisse(caisse, dateDecaissement, search);
    }

    @Override
    public Long montantDateByCaisse(Caisses caisse, Date dateDecaissement) {
        return repository.montantDateByCaisse(caisse, dateDecaissement);
    }

    @Override
    public Double montantByCaisse(Caisses caisse) {
        return repository.montantByCaisse(caisse);
    }

    @Override
    public Double montantDateDecaissements(Date dateDecaissement) {
        return repository.montantDateDecaissements(dateDecaissement);
    }

    @Override
    public Double montantTotalDecaissements() {
        return repository.montantTotalDecaissements();
    }
}
