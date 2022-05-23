package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Encaissements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EncaissementsRepository extends JpaRepository<Encaissements, Long> {

    List<Encaissements> findByDeletedFalse();

    List<Encaissements> findByDeletedFalse(Pageable pageable);

    @Query("SELECT e FROM Encaissements e WHERE (e.dateEncaissement LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.assurance.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (e.deleted = false)")
    List<Encaissements> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Encaissements e WHERE e.deleted = false")
    Long countEncaissements();

    @Query("SELECT COUNT(e) FROM Encaissements e WHERE (e.dateEncaissement LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.assurance.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (e.deleted = false)")
    Long countRecherche(String search);

    List<Encaissements> findByDateEncaissementAndDeletedFalse(Date dateEncaissement, Pageable pageable);

    @Query("SELECT e FROM Encaissements e WHERE (e.facture.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.assurance.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (e.dateEncaissement = :dateEncaissement AND e.deleted = false)")
    List<Encaissements> recherche(final Date dateEncaissement, String search, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Encaissements e WHERE e.dateEncaissement = :dateEncaissement AND e.deleted = false")
    Long countEncaissements(final Date dateEncaissement);

    @Query("SELECT COUNT(e) FROM Encaissements e WHERE (e.facture.fiche.patient.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.telephone LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.typePatient.libelle LIKE CONCAT('%',:search,'%') OR " +
            " e.facture.fiche.patient.assurance.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (e.dateEncaissement = :dateEncaissement AND e.deleted = false)")
    Long countRecherche(final Date dateEncaissement, String search);

    @Query("SELECT SUM(e.montant) FROM Encaissements e where e.deleted = false AND e.dateEncaissement =:startDate")
    Double montantDate(final Date startDate);

}
