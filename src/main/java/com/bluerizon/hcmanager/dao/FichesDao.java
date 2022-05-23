package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Fiches;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.payload.response.FicheResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FichesDao {

    Optional<Fiches> findById(Long id);

    Fiches save(Fiches fiche);

    List<Fiches> findByPatientAndDeletedFalse(final Patients patient, Pageable pageable);

    List<Fiches> recherche(final Patients patient, final String search, Pageable pageable);

    List<FicheResponse> findByPatientAndDeletedFalseRes(final Patients patient, Pageable pageable);

    List<FicheResponse> rechercheRes(final Patients patient, final String search, Pageable pageable);

    Long countFiches(final Patients patient);

    Long countRecherche(final Patients patient, final String search);

}
