package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Kotas;
import com.bluerizon.hcmanager.models.TypeTraitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KotasRepository extends JpaRepository<Kotas, Integer> {

    Kotas findByLibelleAndDeletedFalse(String libelle);
    List<Kotas> findByDeletedFalse();

    List<Kotas> findByDeletedFalse(Pageable pageable);

    @Query("SELECT k FROM Kotas k WHERE k.libelle LIKE CONCAT('%',:search,'%') AND (k.deleted = false)")
    List<Kotas> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(k) FROM Kotas k WHERE k.deleted = false")
    Long countKotas();

    @Query("SELECT COUNT(k) FROM Kotas k WHERE k.libelle LIKE CONCAT('%',:search,'%') AND (k.deleted = false)")
    Long countRecherche(String search);

    Boolean existsByLibelle(String libelle);

}
