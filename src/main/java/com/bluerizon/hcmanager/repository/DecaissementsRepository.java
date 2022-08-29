package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Decaissements;
import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.models.Factures;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DecaissementsRepository extends JpaRepository<Decaissements, Long> {

    List<Decaissements> findByDeletedFalse();

    List<Decaissements> findByDeletedFalse(Pageable pageable);

    @Query("SELECT d FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%') OR " +
            " d.dateDecaissement LIKE CONCAT('%',:search,'%'))" +
            " AND (d.deleted = false)")
    List<Decaissements> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE d.deleted = false")
    Long countDecaissements();

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%') OR " +
            " d.dateDecaissement LIKE CONCAT('%',:search,'%'))" +
            " AND (d.deleted = false)")
    Long countRecherche(String search);

    List<Decaissements> findByDateDecaissementAndDeletedFalse(Date dateDecaissement, Pageable pageable);

    @Query("SELECT d FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%'))" +
            " AND (d.dateDecaissement = :dateDecaissement AND d.deleted = false)")
    List<Decaissements> recherche(final Date dateDecaissement, String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE d.dateDecaissement = :dateDecaissement AND d.deleted = false")
    Long countDecaissements(final Date dateDecaissement);

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%'))" +
            " AND (d.dateDecaissement = :dateDecaissement AND d.deleted = false)")
    Long countRecherche(final Date dateDecaissement, String search);

    @Query("SELECT d FROM Decaissements d WHERE d.ligneCaisse.caissePK.caisse=:caisse AND d.deleted = false")
    List<Decaissements> selectByCaisse(Caisses caisse, Pageable pageable);

    @Query("SELECT d FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%'))" +
            " AND (d.ligneCaisse.caissePK.caisse = :caisse AND d.deleted = false)")
    List<Decaissements> rechercheCaisse(final Caisses caisse, String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE d.ligneCaisse.caissePK.caisse=:caisse AND d.deleted = false")
    Long countByCaisse(Caisses caisse);

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%'))" +
            " AND (d.ligneCaisse.caissePK.caisse = :caisse AND d.deleted = false)")
    Long countRechercheCaisse(final Caisses caisse, String search);

    @Query("SELECT d FROM Decaissements d WHERE d.ligneCaisse.caissePK.caisse=:caisse AND " +
            "d.dateDecaissement = :dateDecaissement AND d.deleted = false")
    List<Decaissements> selectByCaisse(Caisses caisse, final Date dateDecaissement, Pageable pageable);

    @Query("SELECT d FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%'))" +
            " AND (d.ligneCaisse.caissePK.caisse = :caisse AND d.dateDecaissement = :dateDecaissement AND " +
            "d.deleted = false)")
    List<Decaissements> rechercheCaisse(final Caisses caisse, final Date dateDecaissement, String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE d.ligneCaisse.caissePK.caisse=:caisse AND " +
            "d.dateDecaissement = :dateDecaissement AND d.deleted = false")
    Long countByCaisse(Caisses caisse, final Date dateDecaissement);

    @Query("SELECT COUNT(d) FROM Decaissements d WHERE (d.type LIKE CONCAT('%',:search,'%') OR " +
            " d.motif LIKE CONCAT('%',:search,'%') OR d.montant LIKE CONCAT('%',:search,'%'))" +
            " AND (d.ligneCaisse.caissePK.caisse = :caisse AND d.dateDecaissement = :dateDecaissement AND" +
            " d.deleted = false)")
    Long countRechercheCaisse(final Caisses caisse, final Date dateDecaissement, String search);


    @Query("SELECT SUM(d.montant) FROM Decaissements d WHERE d.ligneCaisse.caissePK.caisse=:caisse AND " +
            "d.dateDecaissement = :dateDecaissement AND d.deleted = false")
    Long montantDateByCaisse(Caisses caisse, final Date dateDecaissement);

    @Query("SELECT SUM(d.montant) FROM Decaissements d WHERE d.ligneCaisse.caissePK.caisse=:caisse AND d.deleted = false")
    Double montantByCaisse(Caisses caisse);

    @Query("SELECT SUM(d.montant) FROM Decaissements d WHERE d.dateDecaissement = :dateDecaissement AND d.deleted = false")
    Double montantDateDecaissements(final Date dateDecaissement);

    @Query("SELECT SUM(d.montant) FROM Decaissements d WHERE d.deleted = false")
    Double montantTotalDecaissements();

}
