package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.TypePatients;
import com.bluerizon.hcmanager.models.TypeTraitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TypeTraitementsDao {

    Optional<TypeTraitements> findById(Integer id);

    TypeTraitements save(TypeTraitements traitement);

    List<TypeTraitements> findByDeletedFalse();

    List<TypeTraitements> findByDeletedFalse(Pageable pageable);

    List<TypeTraitements> recherche(String search, Pageable pageable);

    Long countTypeTraitements();

    Long countRecherche(String search);

}
