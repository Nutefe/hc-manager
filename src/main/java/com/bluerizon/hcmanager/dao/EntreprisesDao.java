package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Entreprises;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EntreprisesDao {

    Optional<Entreprises> findById(Long id);

    Entreprises save(Entreprises entreprise);

    List<Entreprises> findByDeletedFalse();

    List<Entreprises> findByDeletedFalse(Pageable pageable);

    List<Entreprises> recherche(String search, Pageable pageable);

    Long countEntreprises();

    Long countRecherche(String search);

}
