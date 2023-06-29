package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.FacturesDao;
import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.repository.FacturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public List<Factures> etatFacture(Assurances assurance, Date start, Date end) {
        return repository.etatFacture(assurance, start, end);
    }

    @Override
    public List<Entreprises> etatEntreprise(Assurances assurance, Date start, Date end) {
        return repository.etatEntreprise(assurance, start, end);
    }

    @Override
    public List<Factures> etatEntreprise(Entreprises entreprise, Assurances assurance, Date start, Date end) {
        return repository.etatEntreprise(entreprise, assurance, start, end);
    }

    @Override
    public Long countDate(Date start) {
        return repository.countDate(start);
    }

    @Override
    public List<Factures> findByDateFactureAndDeletedFalse(Date dateFacture, Pageable pageable) {
        return repository.findByDateFactureAndDeletedFalse(dateFacture, pageable);
    }

    @Override
    public List<Factures> recherche(Date dateFacture, String search, Pageable pageable) {
        return repository.recherche(dateFacture, search, pageable);
    }

    @Override
    public Long countFactures(Date dateFacture) {
        return repository.countFactures(dateFacture);
    }

    @Override
    public Long countRecherche(Date dateFacture, String search) {
        return repository.countRecherche(dateFacture, search);
    }

    @Override
    public Boolean existsByFileName(String fileName) {
        return repository.existsByFileName(fileName);
    }

    @Override
    public boolean existsByFileName(String fileName, Long id) {
        return repository.existsByFileName(fileName, id);
    }

    @Override
    public Factures findByFileName(String fileName) {
        return repository.findByFileName(fileName);
    }

    @Override
    public Double montantDate(Date dateFacture) {
        return repository.montantDate(dateFacture);
    }

    @Override
    public Double acompteDate(Date dateFacture) {
        return repository.acompteDate(dateFacture);
    }

    @Override
    public Double remiseDate(Date dateFacture) {
        return repository.remiseDate(dateFacture);
    }
}
