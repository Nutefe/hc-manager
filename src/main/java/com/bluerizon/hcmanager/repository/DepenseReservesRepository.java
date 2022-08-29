package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.DepenseReserves;
import com.bluerizon.hcmanager.models.Reserves;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DepenseReservesRepository extends JpaRepository<DepenseReserves, Long> {

    List<DepenseReserves> findByDeletedFalse();

    List<DepenseReserves> findByDeletedFalse(Pageable pageable);

    @Query("SELECT d FROM DepenseReserves d WHERE d.motif LIKE CONCAT('%',:search,'%') AND (d.deleted = false)")
    List<DepenseReserves> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM DepenseReserves d WHERE d.deleted = false")
    Long countReserves();

    @Query("SELECT COUNT(d) FROM DepenseReserves d WHERE d.motif LIKE CONCAT('%',:search,'%') AND (d.deleted = false)")
    Long countRecherche(String search);

    @Query("SELECT COUNT(d.montant) FROM DepenseReserves d WHERE d.dateDepense=:dateDepense AND d.deleted = false")
    Double montantReserves(final Date dateDepense);

    @Query("SELECT COUNT(d.montant) FROM DepenseReserves d WHERE d.deleted = false")
    Double montantTotalReserves();

}
