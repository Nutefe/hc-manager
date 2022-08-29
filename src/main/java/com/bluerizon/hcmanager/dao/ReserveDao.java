package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.models.Reserves;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReserveDao {

    Optional<Reserves> findById(Integer id);

    Reserves save(Reserves reserve);

    List<Reserves> findByDeletedFalse();

    List<Reserves> findByDeletedFalse(Pageable pageable);

    List<Reserves> recherche(String search, Pageable pageable);

    Long countReserves();

    Long countRecherche(String search);

}
