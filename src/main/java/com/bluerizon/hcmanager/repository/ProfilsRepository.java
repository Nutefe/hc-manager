package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Profils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfilsRepository extends JpaRepository<Profils, Integer> {

    Optional<Profils> findByLibelle(String libelle);

    List<Profils> findByDeletedFalse();

    @Query("SELECT p FROM Profils p WHERE p.deleted = false And p.id != 1")
    List<Profils> selectProfils();

    @Query("SELECT p FROM Profils p WHERE p.deleted = false And p.id != 1")
    List<Profils> selectProfils(Pageable pageable);

    @Query("SELECT p FROM Profils p WHERE p.libelle LIKE CONCAT('%',:search,'%') AND (p.deleted = false And p.id != 1)")
    List<Profils> recherche(String search, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Profils p WHERE p.deleted = false And p.id != 1")
    Long countProfils();

    @Query("SELECT COUNT(p) FROM Profils p WHERE p.libelle LIKE CONCAT('%',:search,'%') AND (p.deleted = false And p.id != 1) ")
    Long countRecherche(String search);

}
