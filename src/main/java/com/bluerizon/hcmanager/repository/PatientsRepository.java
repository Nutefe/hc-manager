package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.Traitements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientsRepository extends JpaRepository<Patients, Long> {


    List<Patients> findByDeletedFalse();

    List<Patients> findByDeletedFalse(Pageable pageable);
    @Query("SELECT p FROM Patients p WHERE p.deleted = false AND p.typePatient = 3")
    List<Patients> selectPatientNonAssurer();
    @Query("SELECT p FROM Patients p WHERE p.deleted = false AND p.typePatient != 3")
    List<Patients> selectPatientAssurer();

    @Query("SELECT p FROM Patients p WHERE (p.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " p.nom LIKE CONCAT('%',:search,'%') OR " +
            " p.prenom LIKE CONCAT('%',:search,'%') OR " +
            " p.telephone LIKE CONCAT('%',:search,'%') OR " +
            " p.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " p.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " p.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " p.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (p.deleted = false)")
    List<Patients> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Patients p WHERE p.deleted = false")
    Long countPatients();

    @Query("SELECT COUNT(p) FROM Patients p WHERE (p.codeDossier LIKE CONCAT('%',:search,'%') OR " +
            " p.nom LIKE CONCAT('%',:search,'%') OR " +
            " p.prenom LIKE CONCAT('%',:search,'%') OR " +
            " p.telephone LIKE CONCAT('%',:search,'%') OR " +
            " p.utilisateur.username LIKE CONCAT('%',:search,'%') OR " +
            " p.utilisateur.nom LIKE CONCAT('%',:search,'%') OR " +
            " p.utilisateur.prenom LIKE CONCAT('%',:search,'%') OR " +
            " p.typePatient.libelle LIKE CONCAT('%',:search,'%'))" +
            " AND (p.deleted = false)")
    Long countRecherche(String search);

    Boolean existsByCodeDossier(String codeDossier);

    @Query("SELECT CASE WHEN COUNT(codeDossier) > 0 THEN true ELSE false END FROM Patients p WHERE p.codeDossier = :code and p.id != :id")
    boolean existsByCodeDossier(@Param("code") final String code, @Param("id") final Long id);


}
