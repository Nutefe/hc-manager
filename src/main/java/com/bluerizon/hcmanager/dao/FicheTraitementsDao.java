package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Fiches;

import java.util.List;

public interface FicheTraitementsDao {

    FicheTraitements save(final FicheTraitements ficheTraitement);

    List<FicheTraitements> saveAll(final List<FicheTraitements> ficheTraitements);

    List<FicheTraitements> findByFiche(final Fiches fiche);

}
