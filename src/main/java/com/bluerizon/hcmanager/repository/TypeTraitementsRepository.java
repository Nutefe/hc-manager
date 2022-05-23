package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.TypeTraitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeTraitementsRepository extends JpaRepository<TypeTraitements, Integer> {
    List<TypeTraitements> findByDeletedFalse();

    List<TypeTraitements> findByDeletedFalse(Pageable pageable);

    @Query("SELECT t FROM TypeTraitements t WHERE t.libelle LIKE CONCAT('%',:search,'%') AND (t.deleted = false)")
    List<TypeTraitements> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(t) FROM TypeTraitements t WHERE t.deleted = false")
    Long countTypeTraitements();

    @Query("SELECT COUNT(t) FROM TypeTraitements t WHERE t.libelle LIKE CONCAT('%',:search,'%') AND (t.deleted = false)")
    Long countRecherche(String search);
}
