package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.FicheTraitementPK;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Fiches;
import com.bluerizon.hcmanager.models.Patients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FicheTraitementRepository extends JpaRepository<FicheTraitements, FicheTraitementPK> {

    @Query("SELECT f FROM FicheTraitements f WHERE f.ficheTraitementPK.fiche = :fiche AND f.deleted = false")
    List<FicheTraitements> findByFiche(final Fiches fiche);

}
