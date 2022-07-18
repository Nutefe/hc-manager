package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.Patients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Query("SELECT f FROM Factures f WHERE f.deleted = false AND f.fiche.patient.assurance=:assurance AND f.dateFacture BETWEEN :start AND :end  ORDER BY f.createdAt ASC")
    List<Factures> etatFacture(Assurances assurance, Date start, Date end);
    @Query("SELECT DISTINCT f.fiche.patient.entreprise FROM Factures f WHERE f.deleted = false AND f.fiche.patient.assurance=:assurance AND f.dateFacture BETWEEN :start AND :end  ORDER BY f.createdAt ASC")
    List<Entreprises> etatEntreprise(Assurances assurance, Date start, Date end);
    @Query("SELECT  f FROM Factures f WHERE f.deleted = false AND f.fiche.patient.entreprise=:entreprise AND f.fiche.patient.assurance=:assurance AND f.dateFacture BETWEEN :start AND :end  ORDER BY f.createdAt ASC")
    List<Factures> etatEntreprise(Entreprises entreprise, Assurances assurance, Date start, Date end);
    @Query("SELECT  COUNT(f) FROM Factures f WHERE f.deleted = false AND  f.dateFacture = :start")
    Long countDate(final Date start);

    List<Factures> findByDateFactureAndDeletedFalse(Date dateFacture, Pageable pageable);

    @Query("SELECT f FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false AND f.dateFacture=:dateFacture)")
    List<Factures> recherche(Date dateFacture, String search, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Factures f WHERE f.deleted = false AND f.dateFacture=:dateFacture")
    Long countFactures(Date dateFacture);

    @Query("SELECT COUNT(f) FROM Factures f WHERE (f.dateFacture LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " f.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " f.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (f.deleted = false AND f.dateFacture=:dateFacture)")
    Long countRecherche(Date dateFacture, String search);
    Boolean existsByFileName(String fileName);
    @Query("SELECT CASE WHEN COUNT(fileName) > 0 THEN true ELSE false END FROM Factures f WHERE f.fileName = :fileName and f.id != :id")
    boolean existsByFileName(@Param("fileName") final String fileName, @Param("id") final Long id);
    Factures findByFileName(String fileName);

}
