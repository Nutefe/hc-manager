package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Kotas;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KotasDao {

    Optional<Kotas> findById(Integer id);

    Kotas findByLibelleAndDeletedFalse(String libelle);

    Kotas save(Kotas kota);

    List<Kotas> findByDeletedFalse();

    List<Kotas> findByDeletedFalse(Pageable pageable);

    List<Kotas> recherche(String search, Pageable pageable);

    Long countKotas();

    Long countRecherche(String search);

    Boolean existsByLibelle(String libelle);

}
