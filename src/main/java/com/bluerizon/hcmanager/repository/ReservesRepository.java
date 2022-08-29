package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.models.TypePatients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Column;
import java.util.List;

public interface ReservesRepository extends JpaRepository<Reserves, Integer> {

    List<Reserves> findByDeletedFalse();

    List<Reserves> findByDeletedFalse(Pageable pageable);

    @Query("SELECT r FROM Reserves r WHERE r.libelle LIKE CONCAT('%',:search,'%') AND (r.deleted = false)")
    List<Reserves> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Reserves r WHERE r.deleted = false")
    Long countReserves();

    @Query("SELECT COUNT(r) FROM Reserves r WHERE r.libelle LIKE CONCAT('%',:search,'%') AND (r.deleted = false)")
    Long countRecherche(String search);

}
