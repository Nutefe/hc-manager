package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.models.Factures;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EncaissementsDao {

    Optional<Encaissements> findById(Long id);

    Encaissements save(Encaissements encaissement);

    List<Encaissements> findByDeletedFalse();

    List<Encaissements> findByDeletedFalse(Pageable pageable);

    List<Encaissements> findByFactureAndDeletedFalse(Factures facture);

    List<Encaissements> recherche(String search, Pageable pageable);

    Long countEncaissements();

    Long countRecherche(String search);

    List<Encaissements> findByDateEncaissementAndDeletedFalse(Date dateEncaissement, Pageable pageable);

    List<Encaissements> recherche(final Date dateEncaissement, String search, Pageable pageable);

    Long countEncaissements(final Date dateEncaissement);

    Long countRecherche(final Date dateEncaissement, String search);

    Long count();

    Double montantDate(final Date startDate);

    Long countFacture(final Date start);

    Long countPatient(final Date start);

    List<Factures> selectFactureEncaisse(Date start, Pageable pageable);
    List<Factures> rechercheFacture(final Date dateEncaissement, String search, Pageable pageable);
    Long countRechercheFacture(final Date dateEncaissement, String search);

    Double montant();

    Double montantDateEncaissement(final Date dateStart, final Date dateEnd);

}
