package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Factures;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FacturesDao {

    Optional<Factures> findById(Long id);

    Factures save(Factures facture);

    List<Factures> findByDeletedFalse();

    List<Factures> findByDeletedFalse(Pageable pageable);

    List<Factures> recherche(String search, Pageable pageable);

    Long countFactures();
    Long count();

    Long countRecherche(String search);

    List<Factures> findByDeletedFalseAndEncaisseTrue(Pageable pageable);

    List<Factures> rechercheEncaisse(String search, Pageable pageable);

    Long countFacturesEncaisse();

    Long countRechercheEncaisse(String search);

    List<Factures> findByDeletedFalseAndSoldeTrue(Pageable pageable);

    List<Factures> rechercheSolde(String search, Pageable pageable);

    Long countFacturesSolde();

    Long countRechercheSolde(String search);

    List<Factures> findByDeletedFalseAndSoldeFalse(Pageable pageable);
    List<Factures> rechercheSoldeFalse(String search, Pageable pageable);
    Long countFacturesSoldeFalse();
    Long countRechercheSoldeFalse(String search);

    List<Factures> etatFacture(Assurances assurance, Date start, Date end);
    List<Entreprises> etatEntreprise(Assurances assurance, Date start, Date end);
    List<Factures> etatEntreprise(Entreprises entreprise, Assurances assurance, Date start, Date end);
    Long countDate(final Date start);

    List<Factures> findByDateFactureAndDeletedFalse(Date dateFacture, Pageable pageable);
    List<Factures> recherche(Date dateFacture, String search, Pageable pageable);
    Long countFactures(Date dateFacture);
    Long countRecherche(Date dateFacture, String search);
    Boolean existsByFileName(String fileName);
    boolean existsByFileName(@Param("fileName") final String fileName, @Param("id") final Long id);
    Factures findByFileName(String fileName);

}
