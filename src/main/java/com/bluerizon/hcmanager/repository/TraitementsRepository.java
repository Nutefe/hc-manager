package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Traitements;
import com.bluerizon.hcmanager.models.TypePatients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TraitementsRepository extends JpaRepository<Traitements, Integer> {

    List<Traitements> findByDeletedFalse();

    List<Traitements> findByDeletedFalse(Pageable pageable);

    @Query("SELECT t FROM Traitements t WHERE (t.libelle LIKE CONCAT('%',:search,'%') OR " +
            " t.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " t.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " t.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " t.typeTraitement.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (t.deleted = false)")
    List<Traitements> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(t) FROM Traitements t WHERE t.deleted = false")
    Long countTraitements();

    @Query("SELECT COUNT(t) FROM Traitements t WHERE (t.libelle LIKE CONCAT('%',:search,'%') OR " +
            " t.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " t.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " t.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " t.typeTraitement.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (t.deleted = false)")
    Long countRecherche(String search);

}
