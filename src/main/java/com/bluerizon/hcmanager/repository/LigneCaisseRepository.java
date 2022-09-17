package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LigneCaisseRepository extends JpaRepository<LigneCaisses, CaissePK> {

    @Query("SELECT l FROM LigneCaisses l WHERE l.caissePK.utilisateur = :utilisateur")
    LigneCaisses findFirstByUser(final Utilisateurs utilisateur);
    @Query("SELECT l FROM LigneCaisses l WHERE l.caissePK.utilisateur = :utilisateur AND l.deleted = false")
    LigneCaisses findFirstByUserCaisse(final Utilisateurs utilisateur);
    @Query("SELECT l FROM LigneCaisses l WHERE l.caissePK.utilisateur = :utilisateur AND l.deleted = false")
    List<LigneCaisses> findByUser(final Utilisateurs utilisateur);

    @Query("SELECT l FROM LigneCaisses l WHERE l.caissePK.caisse = :caisse AND l.deleted = false")
    List<LigneCaisses> findByCaisse(final Caisses caisse);

}
