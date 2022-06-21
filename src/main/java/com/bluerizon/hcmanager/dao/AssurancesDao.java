package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Assurances;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssurancesDao{

    Optional<Assurances> findById(Integer id);

    Assurances save(Assurances assurance);

    List<Assurances> findByDeletedFalse();

    List<Assurances> findByDeletedFalse(Pageable pageable);

    List<Assurances> recherche(String search, Pageable pageable);

    Long countAssurances();

    Long countRecherche(String search);
    List<Assurances> findAssurerAutre();

}
