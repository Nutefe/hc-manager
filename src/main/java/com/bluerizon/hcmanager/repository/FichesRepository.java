package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Fiches;
import com.bluerizon.hcmanager.models.Patients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FichesRepository extends JpaRepository<Fiches, Long> {

    List<Fiches> findByPatientAndDeletedFalse(final Patients patient, Pageable pageable);

    @Query("SELECT f FROM Fiches f WHERE (f.dateFiche LIKE CONCAT('%',:search,'%'))" +
            " AND (f.patient = :patient AND f.deleted = false)")
    List<Fiches> recherche(final Patients patient, final String search, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Fiches f WHERE f.deleted = false AND f.patient = :patient")
    Long countFiches(final Patients patient);

    @Query("SELECT COUNT(f) FROM Fiches f WHERE (f.dateFiche LIKE CONCAT('%',:search,'%'))" +
            " AND (f.patient = :patient AND f.deleted = false)")
    Long countRecherche(final Patients patient, final String search);
}
