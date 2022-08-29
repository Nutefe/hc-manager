package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LigneCaisseDao {

    Optional<LigneCaisses> selectOne(CaissePK caissePK);

    LigneCaisses findFirstByUser(final Utilisateurs utilisateur);

    LigneCaisses save(LigneCaisses ligneCaisse);

    List<LigneCaisses> findByUser(final Utilisateurs utilisateur);

    List<LigneCaisses> findByCaisse(final Caisses caisse);

}
