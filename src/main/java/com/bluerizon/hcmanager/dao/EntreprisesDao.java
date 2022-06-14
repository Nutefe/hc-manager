package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Entreprises;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Boolean existsByRaisonSocial(String raisonSocial);
    Boolean existsByTelephone(String telephone);
    boolean existsByRaisonSocial(@Param("raisonSocial") final String raisonSocial, @Param("id") final Long id);
    boolean existsByTelephone(@Param("telephone") final String telephone, @Param("id") final Long id);

}
