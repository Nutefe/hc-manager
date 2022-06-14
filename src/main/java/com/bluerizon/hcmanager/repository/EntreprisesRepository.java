package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Traitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Boolean existsByRaisonSocial(String raisonSocial);

    Boolean existsByTelephone(String telephone);

    @Query("SELECT CASE WHEN COUNT(raisonSocial) > 0 THEN true ELSE false END FROM Entreprises e WHERE e.raisonSocial = :raisonSocial and e.id != :id")
    boolean existsByRaisonSocial(@Param("raisonSocial") final String raisonSocial, @Param("id") final Long id);

    @Query("SELECT CASE WHEN COUNT(telephone) > 0 THEN true ELSE false END FROM Entreprises e WHERE e.telephone = :telephone and e.id != :id")
    boolean existsByTelephone(@Param("telephone") final String telephone, @Param("id") final Long id);

}
