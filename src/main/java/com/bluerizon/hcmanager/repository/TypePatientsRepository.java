package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.TypePatients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypePatientsRepository extends JpaRepository<TypePatients, Integer> {

    List<TypePatients> findByDeletedFalse();

    List<TypePatients> findByDeletedFalse(Pageable pageable);

    @Query("SELECT t FROM TypePatients t WHERE t.libelle LIKE CONCAT('%',:search,'%') AND (t.deleted = false)")
    List<TypePatients> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(t) FROM TypePatients t WHERE t.deleted = false")
    Long countTypePatients();

    @Query("SELECT COUNT(t) FROM TypePatients t WHERE t.libelle LIKE CONCAT('%',:search,'%') AND (t.deleted = false)")
    Long countRecherche(String search);

}
