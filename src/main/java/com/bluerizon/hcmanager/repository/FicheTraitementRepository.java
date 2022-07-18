package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FicheTraitementRepository extends JpaRepository<FicheTraitements, FicheTraitementPK> {

    @Query("SELECT f FROM FicheTraitements f WHERE f.ficheTraitementPK.fiche = :fiche AND f.deleted = false")
    List<FicheTraitements> findByFiche(final Fiches fiche);

    @Query("SELECT DISTINCT f.ficheTraitementPK.traitement.typeTraitement FROM FicheTraitements f WHERE f.ficheTraitementPK.fiche = :fiche AND f.deleted = false ORDER BY f.createdAt ASC")
    List<TypeTraitements> findByFicheTraitement(final Fiches fiche);

}
