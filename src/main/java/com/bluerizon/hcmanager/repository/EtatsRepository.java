package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Etats;
import com.bluerizon.hcmanager.models.Kotas;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EtatsRepository extends JpaRepository<Etats, Long> {

    List<Etats> findByDeletedFalse();

    List<Etats> findByDeletedFalse(Pageable pageable);

    @Query("SELECT e FROM Etats e WHERE (e.type LIKE CONCAT('%',:search,'%') OR " +
            "e.assurance.libelle LIKE CONCAT('%',:search,'%')) AND (e.deleted = false)")
    List<Etats> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Etats e WHERE e.deleted = false")
    Long countEtats();

    @Query("SELECT COUNT(e) FROM Etats e WHERE (e.type LIKE CONCAT('%',:search,'%') OR " +
            "e.assurance.libelle LIKE CONCAT('%',:search,'%')) AND (e.deleted = false)")
    Long countRecherche(String search);

}
