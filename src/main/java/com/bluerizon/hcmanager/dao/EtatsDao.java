package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Etats;
import com.bluerizon.hcmanager.models.Kotas;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EtatsDao {

    Optional<Etats> findById(Long id);

    Etats save(Etats etat);

    List<Etats> findByDeletedFalse();

    List<Etats> findByDeletedFalse(Pageable pageable);

    List<Etats> recherche(String search, Pageable pageable);

    Long count();
    Long countEtats();

    Long countRecherche(String search);

}
