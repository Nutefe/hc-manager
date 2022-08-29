package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Encaissements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CaisseDao {


    Optional<Caisses> findById(Integer id);

    Caisses save(Caisses caisse);

    List<Caisses> findByDeletedFalse();

    List<Caisses> findByDeletedFalse(Pageable pageable);

    List<Caisses> recherche(String search, Pageable pageable);

    Long countReserves();

    Long countRecherche(String search);
}
