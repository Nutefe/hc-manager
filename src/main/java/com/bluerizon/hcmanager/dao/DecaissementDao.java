package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Decaissements;
import com.bluerizon.hcmanager.models.Encaissements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DecaissementDao {

    Optional<Decaissements> findById(Long id);

    Decaissements save(Decaissements decaissement);

    List<Decaissements> findByDeletedFalse();

    List<Decaissements> findByDeletedFalse(Pageable pageable);

    List<Decaissements> recherche(String search, Pageable pageable);

    Long countDecaissements();

    Long countRecherche(String search);

    List<Decaissements> findByDateDecaissementAndDeletedFalse(Date dateDecaissement, Pageable pageable);

    List<Decaissements> recherche(final Date dateDecaissement, String search, Pageable pageable);

    Long countDecaissements(final Date dateDecaissement);

    Long countRecherche(final Date dateDecaissement, String search);

    List<Decaissements> selectByCaisse(Caisses caisse, Pageable pageable);

    List<Decaissements> rechercheCaisse(final Caisses caisse, String search, Pageable pageable);

    Long countByCaisse(Caisses caisse);

    Long countRechercheCaisse(final Caisses caisse, String search);

    List<Decaissements> selectByCaisse(Caisses caisse, final Date dateDecaissement, Pageable pageable);

    List<Decaissements> rechercheCaisse(final Caisses caisse, final Date dateDecaissement, String search, Pageable pageable);

    Long countByCaisse(Caisses caisse, final Date dateDecaissement);

    Long countRechercheCaisse(final Caisses caisse, final Date dateDecaissement, String search);

    Long montantDateByCaisse(Caisses caisse, final Date dateDecaissement);

    Double montantByCaisse(Caisses caisse);

    Double montantDateDecaissements(final Date dateDecaissement);

    Double montantTotalDecaissements();
}
