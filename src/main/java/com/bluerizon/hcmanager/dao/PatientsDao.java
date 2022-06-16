package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Patients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientsDao {

    Optional<Patients> findById(Long id);

    Patients save(Patients patient);

    List<Patients> findByDeletedFalse();

    List<Patients> findByDeletedFalse(Pageable pageable);

    List<Patients> recherche(String search, Pageable pageable);

    Long countPatients();

    Long countRecherche(String search);

    Boolean existsByCodeDossier(String codeDossier);

    boolean existsByCodeDossier(final String code, final Long id);

}
