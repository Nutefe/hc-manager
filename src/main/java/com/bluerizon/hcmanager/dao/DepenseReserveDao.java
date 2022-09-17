package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.DepenseReserves;
import com.bluerizon.hcmanager.models.Encaissements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DepenseReserveDao {

    Optional<DepenseReserves> findById(Long id);

    DepenseReserves save(DepenseReserves depenseReserve);

    List<DepenseReserves> findByDeletedFalse();

    List<DepenseReserves> findByDeletedFalse(Pageable pageable);

    List<DepenseReserves> recherche(String search, Pageable pageable);

    Long countReserves();

    Long countRecherche(String search);

    Double montantReserves(final Date dateDepense);

    Double montantTotalReserves();

    Double montantDateDepense(final Date dateStart, final Date dateEnd);
}
