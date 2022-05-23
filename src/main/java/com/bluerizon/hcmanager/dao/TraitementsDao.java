package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Traitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TraitementsDao {

    Optional<Traitements> findById(Integer id);

    Traitements save(Traitements traitement);

    List<Traitements> findByDeletedFalse();

    List<Traitements> findByDeletedFalse(Pageable pageable);

    List<Traitements> recherche(String search, Pageable pageable);

    Long countTraitements();

    Long countRecherche(String search);
}
