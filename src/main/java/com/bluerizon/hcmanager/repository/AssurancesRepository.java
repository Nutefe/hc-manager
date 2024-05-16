package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Assurances;
import com.bluerizon.hcmanager.models.Traitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssurancesRepository extends JpaRepository<Assurances, Integer> {
    List<Assurances> findByDeletedFalse();

    List<Assurances> findByDeletedFalse(Pageable pageable);

    @Query("SELECT a FROM Assurances a WHERE (a.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (a.deleted = false)")
    List<Assurances> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Assurances a WHERE a.deleted = false")
    Long countAssurances();

    @Query("SELECT COUNT(a) FROM Assurances a WHERE (a.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (a.deleted = false)")
    Long countRecherche(String search);

    @Query("SELECT a FROM Assurances a WHERE a.deleted = false AND a.id !=1 AND a.id !=12")
    List<Assurances> findAssurerAutre();

}
