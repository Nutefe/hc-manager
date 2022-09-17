package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Documents;
import com.bluerizon.hcmanager.models.TypeTraitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentsRepository extends JpaRepository<Documents, Integer> {
    List<Documents> findByDeletedFalse();

    List<Documents> findByDeletedFalse(Pageable pageable);

    @Query("SELECT d FROM Documents d WHERE d.libelle LIKE CONCAT('%',:search,'%') AND (d.deleted = false)")
    List<Documents> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Documents d WHERE d.deleted = false")
    Long countDocuments();

    @Query("SELECT COUNT(d) FROM Documents d WHERE d.libelle LIKE CONCAT('%',:search,'%') AND (d.deleted = false)")
    Long countRecherche(String search);
}
