package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.Patients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacturesRepository extends JpaRepository<Factures, Long> {

    List<Factures> findByDeletedFalse();

    List<Factures> findByDeletedFalse(Pageable pageable);

    @Query("SELECT f FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false)")
    List<Factures> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Factures f WHERE f.deleted = false")
    Long countFactures();

    @Query("SELECT COUNT(f) FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false)")
    Long countRecherche(String search);

    List<Factures> findByDeletedFalseAndEncaisseTrue(Pageable pageable);

    @Query("SELECT f FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false and f.encaisse = true)")
    List<Factures> rechercheEncaisse(String search, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Factures f WHERE f.deleted = false AND f.encaisse = true")
    Long countFacturesEncaisse();

    @Query("SELECT COUNT(f) FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false and f.encaisse = true)")
    Long countRechercheEncaisse(String search);

    List<Factures> findByDeletedFalseAndSoldeTrue(Pageable pageable);

    @Query("SELECT f FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false and f.solde = true)")
    List<Factures> rechercheSolde(String search, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Factures f WHERE f.deleted = false AND f.solde = true")
    Long countFacturesSolde();

    @Query("SELECT COUNT(f) FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false and f.solde = true)")
    Long countRechercheSolde(String search);

    List<Factures> findByDeletedFalseAndSoldeFalse(Pageable pageable);

    @Query("SELECT f FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false and f.solde = false)")
    List<Factures> rechercheSoldeFalse(String search, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Factures f WHERE f.deleted = false AND f.solde = false")
    Long countFacturesSoldeFalse();

    @Query("SELECT COUNT(f) FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false and f.solde = false)")
    Long countRechercheSoldeFalse(String search);

}
