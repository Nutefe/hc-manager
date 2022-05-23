package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.TypePatients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TypePatientsDao {

    Optional<TypePatients> findById(Integer id);

    TypePatients save(TypePatients typePatient);

    List<TypePatients> findByDeletedFalse();

    List<TypePatients> findByDeletedFalse(Pageable pageable);

    List<TypePatients> recherche(String search, Pageable pageable);

    Long countTypePatients();

    Long countRecherche(String search);
}
