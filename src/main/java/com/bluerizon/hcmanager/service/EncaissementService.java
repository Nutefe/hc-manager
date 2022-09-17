package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.EncaissementsDao;
import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.repository.EncaissementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EncaissementService implements EncaissementsDao {

    @Autowired
    private EncaissementsRepository repository;

    @Override
    public Optional<Encaissements> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Encaissements save(Encaissements encaissement) {
        return repository.save(encaissement);
    }

    @Override
    public List<Encaissements> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Encaissements> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Encaissements> findByFactureAndDeletedFalse(Factures facture) {
        return repository.findByFactureAndDeletedFalse(facture);
    }

    @Override
    public List<Encaissements> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countEncaissements() {
        return repository.countEncaissements();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public List<Encaissements> findByDateEncaissementAndDeletedFalse(Date dateEncaissement, Pageable pageable) {
        return repository.findByDateEncaissementAndDeletedFalse(dateEncaissement, pageable);
    }

    @Override
    public List<Encaissements> recherche(Date dateEncaissement, String search, Pageable pageable) {
        return repository.recherche(dateEncaissement, search, pageable);
    }

    @Override
    public Long countEncaissements(Date dateEncaissement) {
        return repository.countEncaissements(dateEncaissement);
    }

    @Override
    public Long countRecherche(Date dateEncaissement, String search) {
        return repository.countRecherche(dateEncaissement, search);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Double montantDate(Date startDate) {
        return repository.montantDate(startDate);
    }

    @Override
    public Long countFacture(Date start) {
        return repository.countFacture(start);
    }

    @Override
    public Long countPatient(Date start) {
        return repository.countPatient(start);
    }

    @Override
    public List<Factures> selectFactureEncaisse(Date start, Pageable pageable) {
        return repository.selectFactureEncaisse(start, pageable);
    }

    @Override
    public List<Factures> rechercheFacture(Date dateEncaissement, String search, Pageable pageable) {
        return repository.rechercheFacture(dateEncaissement, search, pageable);
    }

    @Override
    public Long countRechercheFacture(Date dateEncaissement, String search) {
        return repository.countRechercheFacture(dateEncaissement, search);
    }

    @Override
    public Double montant() {
        return repository.montant();
    }

    @Override
    public Double montantDateEncaissement(Date dateStart, Date dateEnd) {
        return repository.montantDateEncaissement(dateStart, dateEnd);
    }
}
