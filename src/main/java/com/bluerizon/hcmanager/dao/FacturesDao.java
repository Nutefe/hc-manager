package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Factures;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FacturesDao {

    Optional<Factures> findById(Long id);

    Factures save(Factures facture);

    List<Factures> findByDeletedFalse();

    List<Factures> findByDeletedFalse(Pageable pageable);

    List<Factures> recherche(String search, Pageable pageable);

    Long countFactures();

    Long countRecherche(String search);

    List<Factures> findByDeletedFalseAndEncaisseTrue(Pageable pageable);

    List<Factures> rechercheEncaisse(String search, Pageable pageable);

    Long countFacturesEncaisse();

    Long countRechercheEncaisse(String search);

    List<Factures> findByDeletedFalseAndSoldeTrue(Pageable pageable);

    List<Factures> rechercheSolde(String search, Pageable pageable);

    Long countFacturesSolde();

    Long countRechercheSolde(String search);

}
