package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Traitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntreprisesRepository extends JpaRepository<Entreprises, Long> {
    List<Entreprises> findByDeletedFalse();

    List<Entreprises> findByDeletedFalse(Pageable pageable);

    @Query("SELECT e FROM Entreprises e WHERE (e.raisonSocial LIKE CONCAT('%',:search,'%') OR " +
            " e.nif LIKE CONCAT('%',:search,'%') OR " +
            " e.telephone LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.prenom LIKE CONCAT('%',:search,'%'))" +
            " AND (e.deleted = false)")
    List<Entreprises> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Entreprises e WHERE e.deleted = false")
    Long countEntreprises();

    @Query("SELECT COUNT(e) FROM Entreprises e WHERE (e.raisonSocial LIKE CONCAT('%',:search,'%') OR " +
            " e.nif LIKE CONCAT('%',:search,'%') OR " +
            " e.telephone LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " e.utilisateur.prenom LIKE CONCAT('%',:search,'%'))" +
            " AND (e.deleted = false)")
    Long countRecherche(String search);
}
