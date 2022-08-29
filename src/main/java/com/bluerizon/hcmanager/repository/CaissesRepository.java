package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Reserves;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaissesRepository extends JpaRepository<Caisses, Integer> {

    List<Caisses> findByDeletedFalse();

    List<Caisses> findByDeletedFalse(Pageable pageable);

    @Query("SELECT c FROM Caisses c WHERE c.libelle LIKE CONCAT('%',:search,'%') AND (c.deleted = false)")
    List<Caisses> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Caisses c WHERE c.deleted = false")
    Long countReserves();

    @Query("SELECT COUNT(c) FROM Caisses c WHERE c.libelle LIKE CONCAT('%',:search,'%') AND (c.deleted = false)")
    Long countRecherche(String search);

}
