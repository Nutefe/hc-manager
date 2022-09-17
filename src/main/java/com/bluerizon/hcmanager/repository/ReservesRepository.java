package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.models.TypePatients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public interface ReservesRepository extends JpaRepository<Reserves, Integer> {

    List<Reserves> findByDeletedFalse();
    Reserves findByCaisseAndDeletedFalse(Caisses caisse);
    Reserves findTop1ByDeletedFalseOrderByIdDesc();
    Reserves findFirst1ByDeletedFalseOrderByIdAsc();
    List<Reserves> findByDeletedFalse(Pageable pageable);

    @Query("SELECT r FROM Reserves r WHERE r.libelle LIKE CONCAT('%',:search,'%') AND (r.deleted = false)")
    List<Reserves> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Reserves r WHERE r.deleted = false")
    Long countReserves();

    @Query("SELECT COUNT(r) FROM Reserves r WHERE r.libelle LIKE CONCAT('%',:search,'%') AND (r.deleted = false)")
    Long countRecherche(String search);

    @Query("SELECT COALESCE(SUM(r.montantReserve), 0) FROM Reserves r WHERE r.dateReserve = :dateReserve AND r.deleted = false")
    Double montantDateReserves(final Date dateReserve);

    @Query("SELECT COALESCE(SUM(r.montantReserve), 0) FROM Reserves r WHERE r.deleted = false")
    Double montantTotalReserves();

    @Query("SELECT COALESCE(SUM(r.montantReserve), 0) FROM Reserves r WHERE r.dateReserve BETWEEN :dateStart AND :dateEnd  AND r.deleted = false")
    Double montantDateReserve(final Date dateStart, final Date dateEnd);

}
