package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Documents;
import com.bluerizon.hcmanager.models.TypeTraitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentsDao {

    Optional<Documents> findById(Integer id);

    Documents save(Documents document);

    List<Documents> findByDeletedFalse();

    List<Documents> findByDeletedFalse(Pageable pageable);

    List<Documents> recherche(String search, Pageable pageable);

    Long countDocuments();

    Long countRecherche(String search);

}
