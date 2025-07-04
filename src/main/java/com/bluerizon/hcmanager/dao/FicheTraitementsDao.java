package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Fiches;
import com.bluerizon.hcmanager.models.TypeTraitements;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FicheTraitementsDao {

    FicheTraitements save(final FicheTraitements ficheTraitement);

    List<FicheTraitements> saveAll(final List<FicheTraitements> ficheTraitements);

    List<FicheTraitements> findByFiche(final Fiches fiche);
    List<TypeTraitements> findByFicheTraitement(final Fiches fiche);

    void delete(final List<FicheTraitements> ficheTraitements);

}
